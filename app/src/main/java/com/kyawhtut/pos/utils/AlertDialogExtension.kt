package com.kyawhtut.pos.utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog

object AlertDialogExtension

class DialogClick private constructor(
    val text: String,
    val onClick: (DialogInterface) -> Unit
) {
    class Builder {
        var text: String = ""
        var onClick: (DialogInterface) -> Unit = {}

        fun build() = DialogClick(text, onClick)
    }
}

fun dialogClick(block: DialogClick.Builder.() -> Unit) =
    DialogClick.Builder().apply(block).build()

private fun AlertDialog.Builder.setPositiveButton(block: (DialogClick.Builder.() -> Unit)? = null) {
    if (block != null) {
        val positive = DialogClick.Builder().apply(block).build()
        setPositiveButton(positive.text) { dialog, which ->
            positive.onClick(dialog)
        }
    }
}

private fun AlertDialog.Builder.setNegativeButton(block: (DialogClick.Builder.() -> Unit)? = null) {
    if (block != null) {
        val negative = DialogClick.Builder().apply(block).build()
        setNegativeButton(negative.text) { dialog, which ->
            negative.onClick(dialog)
        }
    }
}

fun Context?.showDialog(
    title: String? = null,
    message: String,
    isCancelable: Boolean = true,
    onClickPositive: (DialogClick.Builder.() -> Unit)? = null,
    onClickNegative: (DialogClick.Builder.() -> Unit)? = null
) {
    AlertDialog.Builder(checkNull()).apply {
        if (title != null)
            setTitle(title)
        setMessage(message)
        setCancelable(isCancelable)
        setPositiveButton(onClickPositive)
        setNegativeButton(onClickNegative)
    }.show()
}

fun Context?.showDialog(
    view: View,
    bindView: View.(AlertDialog) -> Unit = {},
    isCancelable: Boolean = true,
    autoDismiss: Boolean = false,
    isTransparent: Boolean = false,
    onClickPositive: (DialogClick.Builder.() -> Unit)? = null,
    onClickNegative: (DialogClick.Builder.() -> Unit)? = null
) {
    val dialog = AlertDialog.Builder(checkNull()).apply {
        setView(view)
        setCancelable(isCancelable)
        setPositiveButton(onClickPositive)
        setNegativeButton(onClickNegative)
    }.create()

    if (isTransparent)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    if (autoDismiss) {
        Handler().postDelayed(
            {
                dialog.dismiss()
            },
            2000
        )
    }

    view.bindView(dialog)

    dialog.show()
}
