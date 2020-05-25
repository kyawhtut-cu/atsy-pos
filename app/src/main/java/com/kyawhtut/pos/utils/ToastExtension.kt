package com.kyawhtut.pos.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

object ToastExtension

private fun Context?.createToast(message: String, duration: Int): Toast =
    Toast.makeText(this, message, duration)

private fun Context?.showToast(message: String, duration: Int) =
    createToast(message, duration).show()

fun Context?.shortToast(message: String) = showToast(message, Toast.LENGTH_SHORT)

fun Context?.longToast(message: String) = showToast(message, Toast.LENGTH_LONG)

fun Context?.toastFor(message: String, duration: Int) = createToast(message, duration)

fun Fragment.shortToast(message: String) = context.showToast(message, Toast.LENGTH_SHORT)

fun Fragment.longToast(message: String) = context.showToast(message, Toast.LENGTH_LONG)

fun Fragment.toastFor(message: String, duration: Int) = context.createToast(message, duration)