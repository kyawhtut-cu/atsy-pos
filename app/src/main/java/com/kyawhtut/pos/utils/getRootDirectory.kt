package com.kyawhtut.pos.utils

import android.content.Context
import android.graphics.*
import android.os.Environment
import android.util.LruCache
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kyawhtut.pos.R
import java.io.File
import java.io.FileOutputStream

fun Context.getRootDirectory(path: String = "") = File(
    Environment.getExternalStorageDirectory(),
    getString(R.string.app_name).takeIf { path.isEmpty() }
        ?: getString(R.string.app_name) + File.separator + path
)

fun View.getBitmap(width: Int, height: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val drawable = this.background
    if (drawable != null) {
        drawable.draw(canvas)
    } else {
        canvas.drawColor(Color.WHITE)
    }
    this.draw(canvas)
    return bitmap
}

fun Bitmap.saveToFile(
    ctx: Context,
    fileName: String = "%s.jpg".format(System.currentTimeMillis())
): String {
    val folder = ctx.getRootDirectory("image")
    if (!folder.exists()) {
        folder.mkdirs()
    }
    val outputFile = File(folder, fileName)
    return try {
        val output = FileOutputStream(outputFile)
        this.compress(Bitmap.CompressFormat.JPEG, 100, output)
        output.flush()
        output.close()
        outputFile.path
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun RecyclerView.getBitmap(callback: Bitmap.(Int, Int) -> Unit = { _, _ -> }): Bitmap {
    var bitmap: Bitmap? = null
    val adapter = this.adapter
    adapter?.let {
        var height = 0
        val paint = Paint()
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        val cacheSize = (maxMemory / 8)
        val bitmapCache = LruCache<String, Bitmap>(cacheSize)
        for (i in 0 until adapter.itemCount) {
            val holder = adapter.createViewHolder(this, adapter.getItemViewType(i))
            adapter.onBindViewHolder(holder, i)
            holder.itemView.measure(
                View.MeasureSpec.makeMeasureSpec(this.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            holder.itemView.layout(
                0,
                0,
                holder.itemView.measuredWidth,
                holder.itemView.measuredHeight
            )
            holder.itemView.isDrawingCacheEnabled = true
            val drawingCache = holder.itemView.drawingCache
            if (drawingCache != null) {
                bitmapCache.put("$i", drawingCache)
            }
            /*holder.itemView.isDrawingCacheEnabled = false
            holder.itemView.destroyDrawingCache()*/
            height += holder.itemView.measuredHeight
        }
        bitmap = Bitmap.createBitmap(this.measuredWidth, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap!!)
        canvas.drawColor(Color.WHITE)
        height = 0
        for (i in 0 until adapter.itemCount) {
            val item = bitmapCache.get("$i")
            canvas.drawBitmap(item, 0f, height.toFloat(), paint)
            height += item.height
            item.recycle()
        }
        bitmap?.callback(this.measuredWidth, height)
    }
    return bitmap!!
}

fun Bitmap.toGreyImage(): Bitmap {
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    this.getPixels(pixels, 0, width, 0, 0, width, height)
    var redSum = 0.0
    var greenSum = 0.0
    var blueSun = 0.0
    val total = width * height

    for (i in 0 until height) {
        for (j in 0 until width) {
            val grey = pixels[width * i + j]

            val red = grey and 0x00FF0000 shr 16
            val green = grey and 0x0000FF00 shr 8
            val blue = grey and 0x000000FF

            redSum += red.toDouble()
            greenSum += green.toDouble()
            blueSun += blue.toDouble()
        }
    }
    val m = (redSum / total).toInt()

    // Conversion monochrome diagram
    for (i in 0 until height) {
        for (j in 0 until width) {
            var grey = pixels[width * i + j]

            val alpha1 = 0xFF shl 24
            var red = grey and 0x00FF0000 shr 16
            var green: Int
            var blue: Int

            if (red >= m) {
                blue = 255
                green = blue
                red = green
            } else {
                blue = 0
                green = blue
                red = green
            }
            grey = alpha1 or (red shl 16) or (green shl 8) or blue
            pixels[width * i + j] = grey
        }
    }
    val mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    mBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return mBitmap
}

fun Bitmap.resize(w: Int, isChecked: Boolean): Bitmap {
    val orgBitmap = this
    val width = orgBitmap.width
    val height = orgBitmap.height
    if (width <= w) {
        return this
    }
    var resizeBitmap: Bitmap
    if (isChecked) {
        resizeBitmap = Bitmap.createBitmap(orgBitmap, 0, 0, w, height)
    } else {
        val scaleWidth = w.toFloat() / width.toFloat()
        val scaleHeight = ((height * w).toFloat() / width) / height.toFloat()
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        resizeBitmap = Bitmap.createBitmap(orgBitmap, 0, 0, width, height, matrix, true)
    }
    return resizeBitmap
}
