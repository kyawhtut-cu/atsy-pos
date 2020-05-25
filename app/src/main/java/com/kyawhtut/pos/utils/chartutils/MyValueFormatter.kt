package com.kyawhtut.pos.utils.chartutils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

/**
 * @author kyawhtut
 * @date 14/05/2020
 */
class MyValueFormatter(private val suffix: String = "") : ValueFormatter() {

    private val format by lazy { DecimalFormat("##,###,###,##0.0") }

    override fun getFormattedValue(value: Float): String {
        return "%s%s".format(value.toInt(), suffix)
    }

    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        return when {
            axis is XAxis -> value.toInt().toString()//format.format(value)
            value > 0 -> getFormattedValue(value)
            else -> value.toInt().toString()//format.format(value)
        }
    }
}
