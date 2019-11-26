package com.kyawhtut.lib.rv

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.enableSwipe(
        title: String,
        message: String,
        ctx: Context,
        onDelete: (Int) -> Unit,
        onCancel: (Int) -> Unit
) {
    ItemTouchHelper(object : SwipeToDeleteCallback(ctx) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.adapterPosition
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    AlertDialog.Builder(ctx).apply {
                        setTitle(title)
                        setMessage(message)
                        setPositiveButton("DELETE") { view, _ ->
                            view.dismiss()
                            onDelete(pos)
                        }
                        setNegativeButton("Cancel") { view, _ ->
                            view.dismiss()
                            onCancel(pos)
                        }
                        setOnCancelListener {
                            it.dismiss()
                            onCancel(pos)
                        }
                    }.show()
                }
                ItemTouchHelper.RIGHT -> {
                    /*onEdit(pos)*/
                }
            }
        }
    }).attachToRecyclerView(this)
}