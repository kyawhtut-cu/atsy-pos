package com.kyawhtut.pos.utils

import android.content.Context
import android.graphics.Color
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object ResourceExtension

fun Context?.checkNull() =
    this ?: throw Exception("Context must not be null")

fun String.toColor() = Color.parseColor(this)

fun Int.toHexString() = "#%06X".format((0xFFFFFF and this))

fun Context?.getStringList(@ArrayRes res: Int): Array<String> =
    checkNull().resources.getStringArray(res)

fun Context?.getIntList(@ArrayRes res: Int) = checkNull().resources.getIntArray(res)

fun Context?.getColorValue(@ColorRes res: Int) = ContextCompat.getColor(checkNull(), res)

fun Context?.getDrawableValue(@DrawableRes res: Int) = ContextCompat.getDrawable(checkNull(), res)

fun Fragment.getColorValue(@ColorRes res: Int) = requireContext().getColorValue(res)

@ColorInt
fun Context?.getColorFromAttr(@AttrRes res: Int): Int {
    val typedArray = checkNull().obtainStyledAttributes(intArrayOf(res))
    return typedArray.getColor(0, Color.TRANSPARENT).also {
        typedArray.recycle()
    }
}
