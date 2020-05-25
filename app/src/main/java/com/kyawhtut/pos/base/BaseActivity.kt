package com.kyawhtut.pos.base

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.kyawhtut.fontchooserlib.FontChoose
import com.kyawhtut.pos.R
import com.kyawhtut.pos.service.ServiceInterface
import com.kyawhtut.pos.ui.devicedialog.DeviceListDialog
import com.kyawhtut.pos.ui.scanner.ScannerActivity
import com.kyawhtut.pos.utils.*
import com.yanzhenjie.loading.dialog.LoadingDialog
import com.zxy.tiny.Tiny
import net.posprinter.posprinterface.IMyBinder
import net.posprinter.posprinterface.UiExecute
import net.posprinter.service.PosprinterService
import net.posprinter.utils.BitmapToByteData
import net.posprinter.utils.DataForSendToPrinterPos80

abstract class BaseActivity(
    @LayoutRes private val layoutId: Int,
    @MenuRes private val menuId: Int? = null,
    @IdRes private val toolbar: Int? = null,
    private val isBackAction: Boolean = false
) : AppCompatActivity(), ServiceInterface {

    companion object {
        private const val extraScannerRequest = 0x001
        private const val extraBluetoothEnable = 0x002
        var binder: IMyBinder? = null
        var isConnect: Boolean = false
    }

    private lateinit var loading: LoadingDialog
    abstract fun setup(savedInstanceState: Bundle?, bundle: Bundle)
    open fun onClickMenu(id: Int) {}
    protected var menuCamera: MenuItem? = null
    private var menuAdd: MenuItem? = null
    private var menuSearch: MenuItem? = null

    var scanResult: (String) -> Unit = {}

    protected val conn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            binder = service as IMyBinder
        }
    }
    val bluetoothAdapter: BluetoothAdapter by lazy { BluetoothAdapter.getDefaultAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FontChoose.init(this, R.drawable.ic_g_letter)
        setContentView(layoutId)
        if (toolbar != null)
            setSupportActionBar(findViewById(toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(isBackAction)

        loading = LoadingDialog(this)

        setup(savedInstanceState, intent.extras ?: Bundle())

        /*if (!isServiceRun) {
            isServiceRun = true
            bindService(intentFor<PosprinterService>(), conn, Context.BIND_AUTO_CREATE)
        }*/
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(FontChoose.updateBaseContextLocale(newBase))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menuId != null) {
            menuInflater.inflate(menuId, menu)
            menuAdd = menu.findItem(R.id.action_add)
            menuCamera = menu.findItem(R.id.action_camera)
            menuSearch = menu.findItem(R.id.action_search)

            hideAllMenuItem()
        }
        return true
    }

    protected fun hideAllMenuItem() {
        menuSearch?.isVisible = false
        menuAdd?.isVisible = false
        menuCamera?.isVisible = false
    }

    protected fun showAllMenuItem() {
        menuSearch?.isVisible = true
        menuAdd?.isVisible = true
        menuCamera?.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> onBackPressed().run {
                true
            }
            else -> onClickMenu(item.itemId).run {
                true
            }
        }
    }

    fun printNow(bitmap: Bitmap, success: () -> Unit, fail: () -> Unit) {
        if (!isConnect) {
            initBluetooth(bitmap, success, fail)
            return
        }
        try {
            with(bitmap.toGreyImage()) {
                Tiny.getInstance().source(this).asBitmap()
                    .withOptions(Tiny.BitmapCompressOptions())
                    .compress { isSuccess, bitmap, t ->
                        if (isSuccess) {
                            binder?.writeDataByYouself(object : UiExecute {
                                override fun onsucess() {
                                    success()
                                }

                                override fun onfailed() {
                                    fail()
                                }
                            }) {
                                val list = arrayListOf<ByteArray>()
                                list.apply {
                                    add(DataForSendToPrinterPos80.initializePrinter())
                                    add(
                                        DataForSendToPrinterPos80.printRasterBmp(
                                            0,
                                            this@with.resize(550, false),
                                            BitmapToByteData.BmpType.Threshold,
                                            BitmapToByteData.AlignType.Center,
                                            576
                                        )
                                    )
                                    add(DataForSendToPrinterPos80.printAndFeedForward(1))
                                    add(
                                        DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(
                                            25,
                                            1
                                        )
                                    )
                                }
                                list
                            }
                        }
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initBluetooth(bitmap: Bitmap, success: () -> Unit, fail: () -> Unit) {
        if (!bluetoothAdapter.isEnabled) {
            startActivityForResult(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                extraBluetoothEnable
            )
        } else {
            // show bluetooth list
            DeviceListDialog().apply {
                setDelegate {
                    connectBluetooth(it) {
                        printNow(bitmap, success, fail)
                    }
                }
                show(
                    supportFragmentManager,
                    "DeviceList"
                )
            }
        }
    }

    private fun connectBluetooth(ipAddress: String, callback: () -> Unit) {
        showDialog()
        binder?.connectBtPort(ipAddress, object : UiExecute {
            override fun onfailed() {
                closeDialog()
                isConnect = false
                longToast("Connect failed.")
            }

            override fun onsucess() {
                closeDialog()
                isConnect = true
                longToast("Connect success.")
                callback()
                binder?.write(
                    DataForSendToPrinterPos80.openOrCloseAutoReturnPrintState(0x1f),
                    object : UiExecute {
                        override fun onfailed() {
                            isConnect = false
                            longToast("Connection has disconnected.")
                        }

                        override fun onsucess() {
                        }
                    })
            }
        })
    }

    fun openScanner() {
        askPermission(Manifest.permission.CAMERA) {
            startActivityForResult<ScannerActivity>(extraScannerRequest)
        }.onDeclined {
            if (it.hasDenied()) {
                showDialog(
                    message = "Please accept camera permission",
                    isCancelable = false,
                    onClickPositive = {
                        text = "Ok"
                        onClick = { _ ->
                            it.askAgain()
                        }
                    }
                )
            }
            if (it.hasForeverDenied()) {
                showDialog(
                    message = "Please open camera permission in application setting.",
                    onClickPositive = {
                        text = "Ok"
                        onClick = {
                            openPermission()
                        }
                    },
                    onClickNegative = {
                        text = "Cancel"
                        onClick = {
                            it.dismiss()
                        }
                    }
                )
            }
        }
    }

    fun showDialog() {
        if (!loading.isShowing) {
            loading.show()
        }
    }

    fun closeDialog() {
        if (loading.isShowing) loading.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == extraScannerRequest && resultCode == Activity.RESULT_OK) {
            scanResult(data?.getStringExtra(ScannerActivity.extraProductCode) ?: "")
        }
    }
}
