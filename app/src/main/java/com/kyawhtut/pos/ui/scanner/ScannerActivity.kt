package com.kyawhtut.pos.ui.scanner

import android.Manifest
import android.app.Activity
import android.os.Bundle
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.zxing.Result
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseActivity
import com.kyawhtut.pos.utils.longToast
import kotlinx.android.synthetic.main.activity_scanner.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScannerActivity : BaseActivity(R.layout.activity_scanner), ZXingScannerView.ResultHandler {

    companion object {
        const val extraProductCode = "extra.ProductCode"
    }

    override fun setup(savedInstanceState: Bundle?, bundle: Bundle) {
        askPermission(Manifest.permission.CAMERA) {
        }.onDeclined {
            longToast("Please open camera permission.")
            setResult(Activity.RESULT_CANCELED)
            finish()
            return@onDeclined
        }

        scanner_view.apply {
            setResultHandler(this@ScannerActivity)
            setAutoFocus(true)
            startCamera()
        }

    }

    override fun onResume() {
        super.onResume()
        scanner_view.startCamera()
    }

    override fun onStop() {
        super.onStop()
        scanner_view.stopCamera()
    }

    override fun handleResult(result: Result) {
        intent.putExtra(extraProductCode, result.text)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}