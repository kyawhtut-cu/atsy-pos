package com.kyawhtut.pos.utils.chartutils

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.kyawhtut.pos.R
import kotlinx.android.synthetic.main.custom_marker_view.view.*
import java.text.DecimalFormat

/**
 * @author kyawhtut
 * @date 14/05/2020
 */
class XYMarkerView(context: Context, private val formatter: ValueFormatter) :
    MarkerView(context, R.layout.custom_marker_view) {

    private val format by lazy { DecimalFormat("###.0") }

    override fun refreshContent(e: Entry, highlight: Highlight?) {
        tvContent.text = e.data.toString()
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}
