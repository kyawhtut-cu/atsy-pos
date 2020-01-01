package com.kyawhtut.pos.base

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.kyawhtut.fontchooserlib.FontChoose
import com.kyawhtut.pos.R
import com.kyawhtut.pos.ui.scanner.ScannerActivity
import com.kyawhtut.pos.utils.openPermission
import com.kyawhtut.pos.utils.showDialog
import com.kyawhtut.pos.utils.startActivityForResult

abstract class BaseActivity(
    @LayoutRes private val layoutId: Int,
    @MenuRes private val menuId: Int? = null,
    @IdRes private val toolbar: Int? = null,
    private val isBackAction: Boolean = false
) : AppCompatActivity() {

    companion object {
        private const val extraScannerRequest = 0x001
    }

    abstract fun setup(savedInstanceState: Bundle?, bundle: Bundle)
    open fun onClickMenu(id: Int) {}
    protected var menuCamera: MenuItem? = null
    private var menuAdd: MenuItem? = null
    private var menuSearch: MenuItem? = null

    var scanResult: (String) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FontChoose.init(this, R.drawable.ic_g_letter)
        setContentView(layoutId)
        if (toolbar != null)
            setSupportActionBar(findViewById(toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(isBackAction)

        setup(savedInstanceState, intent.extras ?: Bundle())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == extraScannerRequest && resultCode == Activity.RESULT_OK) {
            scanResult(data?.getStringExtra(ScannerActivity.extraProductCode) ?: "")
        }
    }
}
