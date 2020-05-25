package com.kyawhtut.pos.ui.colortest

import android.graphics.Color
import kotlin.math.abs

object ColorTool {

    fun closeMatch(firstColor: Int, secondColor: Int, n3: Int): Boolean {
        return abs(Color.red(firstColor) - Color.red(secondColor)) <= n3 && abs(
            Color.green(firstColor) - Color.green(secondColor)
        ) <= n3 && abs(
            Color.blue(firstColor) - Color.blue(
                secondColor
            )
        ) <= n3
    }
}
