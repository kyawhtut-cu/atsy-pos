package com.kyawhtut.pos.ui.colortest

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.kyawhtut.pos.R
import com.kyawhtut.pos.utils.longToast
import com.kyawhtut.pos.utils.toColor
import kotlinx.android.synthetic.main.activity_color_test.*
import timber.log.Timber

/**
 * @author kyawhtut
 * @date 08/05/2020
 */
class ColorTestActivity : AppCompatActivity(), View.OnTouchListener {

    private var isDialogShow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_test)
        iv_color.setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val action = event.action
        val x = event.x.toInt()
        val y = event.y.toInt()
        val obj = iv_color.tag
        val tag: Int? = if (obj == null) null else obj as Int
        val intValue = tag ?: R.drawable.revised_default
        var b = true
        var imageResource: Int
        when (action) {
            0 -> {
                if (intValue == R.drawable.revised_default) {
                    b = false
                    imageResource = -1
                } else {
                    // dismiss dialog
                    isDialogShow = false
                    imageResource = R.drawable.revised_default
                }
            }
            1 -> {
                imageResource = R.drawable.revised_default
                val hotspotColor = getHotspotColor(iv_color, x, y)
                when {
                    ColorTool.closeMatch("#000000".toColor(), hotspotColor, 25) -> {
//                        longToast("This is black color.")
                        Timber.d("This is black color.")
                    }
                    ColorTool.closeMatch("#ffffff".toColor(), hotspotColor, 25) -> {
//                        longToast("This is white color.")
                        Timber.d("This is white color.")
                    }
                    ColorTool.closeMatch("#ff0000".toColor(), hotspotColor, 25) -> {
//                        longToast("This is red color.")
                        Timber.d("This is red color.")
                    }
                    ColorTool.closeMatch("#00ff00".toColor(), hotspotColor, 25) -> {
//                        longToast("This is green color.")
                        Timber.d("This is green color.")
                    }
                    ColorTool.closeMatch("#0000ff".toColor(), hotspotColor, 25) -> {
//                        longToast("This is blue color.")
                        Timber.d("This is blue color.")
                    }
                    ColorTool.closeMatch("#ffff00".toColor(), hotspotColor, 25) -> {
//                        longToast("This is yellow color.")
                        Timber.d("This is yellow color.")
                    }
                    ColorTool.closeMatch("#00ffff".toColor(), hotspotColor, 25) -> {
//                        longToast("This is light blue color.")
                        Timber.d("This is light blue color.")
                    }
                    ColorTool.closeMatch("#ff00ff".toColor(), hotspotColor, 25) -> {
//                        longToast("This is pink color.")
                        Timber.d("This is pink color.")
                    }
                    ColorTool.closeMatch("#800000".toColor(), hotspotColor, 25) -> {
//                        longToast("This is dark red color.")
                        Timber.d("This is dark red color.")
                    }
                    ColorTool.closeMatch("#808000".toColor(), hotspotColor, 25) -> {
//                        longToast("This is dark yellow color.")
                        Timber.d("This is dark yellow color.")
                    }
                }
            }
            else -> {
                // dismiss dialog
                /*
                if (this.mypopup != null) {
                            if (this.isDialogShow) {
                                this.mypopup.dismiss()
                            }
                            this.isDialogShow = false
                        }
                */
                b = false
                imageResource = -1
                /*if (this.IspopupOpen) {
                                this.mypopup.dismiss();
                            }
                            this.IspopupOpen = false;
                            break;*/
            }
        }
        if (b && imageResource > 0) {
            iv_color.tag = imageResource
        }
        return true
    }

    private fun getHotspotColor(image: ImageView, x: Int, y: Int): Int {
        image.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(image.drawingCache)
        return bitmap?.let {
            image.isDrawingCacheEnabled = false
            bitmap.getPixel(x, y)
        } ?: 0
    }
}
