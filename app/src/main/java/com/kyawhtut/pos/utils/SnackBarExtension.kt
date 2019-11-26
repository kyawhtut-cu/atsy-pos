package com.kyawhtut.pos.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackBarExtension

private fun View.createSnackBar(
    message: String,
    duration: Int,
    action: String?,
    click: View.(Snackbar) -> Unit = {}
): Snackbar = Snackbar.make(
    this, message, duration
).apply {
    if (action != null) {
        setAction(action) {
            it.click(this)
        }
    }
}

private fun View.showSnackBar(
    message: String,
    duration: Int,
    action: String?,
    click: View.(Snackbar) -> Unit = {}
) = createSnackBar(message, duration, action, click).show()

fun View.shortSnackBar(
    message: String,
    action: String?,
    click: View.(Snackbar) -> Unit = {}
) = showSnackBar(message, Snackbar.LENGTH_SHORT, action, click)

fun View.longSnackBar(
    message: String,
    action: String? = null,
    click: View.(Snackbar) -> Unit = {}
) = showSnackBar(message, Snackbar.LENGTH_LONG, action, click)

fun View.indefiniteSnackBar(
    message: String,
    action: String? = null,
    click: View.(Snackbar) -> Unit = {}
) = showSnackBar(message, Snackbar.LENGTH_INDEFINITE, action, click)

fun View.snackBarFor(
    message: String,
    duration: Int,
    action: String? = null,
    click: View.(Snackbar) -> Unit = {}
) = createSnackBar(message, duration, action, click)
