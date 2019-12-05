package com.kyawhtut.pos.utils

import android.content.Context
import android.graphics.Color
import androidx.annotation.*
import androidx.core.content.ContextCompat

object ResourceExtension

fun Context?.checkNull() =
    this ?: throw Exception("Context must not be null")

fun String.toColor() = Color.parseColor(this)

fun Context?.getStringList(@ArrayRes res: Int) = checkNull().resources.getStringArray(res)

fun Context?.getIntList(@ArrayRes res: Int) = checkNull().resources.getIntArray(res)

fun Context?.getColorValue(@ColorRes res: Int) = ContextCompat.getColor(checkNull(), res)

fun Context?.getDrawableValue(@DrawableRes res: Int) = ContextCompat.getDrawable(checkNull(), res)

@ColorInt
fun Context?.getColorFromAttr(@AttrRes res: Int): Int {
    val typedArray = checkNull().obtainStyledAttributes(intArrayOf(res))
    return typedArray.getColor(0, Color.TRANSPARENT).also {
        typedArray.recycle()
    }
}
