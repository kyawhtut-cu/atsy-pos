package com.kyawhtut.pos.ui.view

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.kyawhtut.pos.R
import com.kyawhtut.pos.utils.chartutils.DayAxisValueFormatter
import com.kyawhtut.pos.utils.chartutils.MyValueFormatter
import com.kyawhtut.pos.utils.chartutils.XYMarkerView
import com.kyawhtut.pos.utils.color.RandomColor
import com.kyawhtut.pos.utils.getInflateView
import com.kyawhtut.pos.utils.gone
import com.kyawhtut.pos.utils.visible
import kotlinx.android.synthetic.main.component_bar_chart.view.*

/**
 * @author kyawhtut
 * @date 14/05/2020
 */
class ComponentBarChart : FrameLayout, OnChartValueSelectedListener {

    var onFilterSelected: (String) -> Unit = {}

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addView(context.getInflateView(R.layout.component_bar_chart, this))
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.ComponentBarChart, defStyleAttr, 0)
        try {
            titleText = a.getString(R.styleable.ComponentEmptyView_android_text) ?: ""
        } finally {
            a.recycle()
        }
    }

    private var titleText: String = ""
        set(value) {
            field = value
            tv_title.mText = value
        }

    fun setTitle(value: String) {
        titleText = value
    }

    fun setTitle(resId: Int) {
        titleText = context.getString(resId)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setFilter(listOf())
        sp_filter_type.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            onFilterSelected(parent.selectedItem as String)
        }
        tv_title.setOnClickListener { }
        bar_chart.apply {
            setOnChartValueSelectedListener(this@ComponentBarChart)
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            description.isEnabled = false
            setMaxVisibleValueCount(0)
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false
            setDrawGridBackground(false)

            val xAxisFormatter = DayAxisValueFormatter(this)
            xAxis.apply {
                isEnabled = false
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                labelCount = 7
                valueFormatter = xAxisFormatter
            }

            val customValueFormatter = MyValueFormatter()
            axisLeft.apply {
                setLabelCount(8, false)
                valueFormatter = customValueFormatter
                setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                spaceTop = 15f
                axisMinimum = 0f
            }

            axisRight.apply {
                isEnabled = false
                setDrawGridLines(false)
                setLabelCount(8, false)
                valueFormatter = customValueFormatter
                spaceTop = 15f
                axisMinimum = 0f
            }

            legend.apply {
                isEnabled = false
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
                form = Legend.LegendForm.CIRCLE
                formSize = 9f
                textSize = 11f
                xEntrySpace = 4f
            }

            val mv = XYMarkerView(this@ComponentBarChart.context, xAxisFormatter)
            mv.chartView = this
            bar_chart.marker = mv
        }
        setData(listOf(), listOf(), listOf())
    }

    fun setFilter(filterList: List<String>) {
        sp_filter_type.attachDataSource(filterList)
        if (filterList.isEmpty()) sp_filter_type.gone()
        else sp_filter_type.visible()
    }

    fun setData(data: List<Float>, colors: List<Int>, value: List<String>) {
        if (data.isEmpty()) empty_view.visible().run {
            return
        }
        empty_view.gone()
        val set1 = BarDataSet(
            data.mapIndexed { index, amount -> BarEntry(index.toFloat(), amount, value[index]) },
            "The year 2017"
        )
        set1.setDrawIcons(false)
        set1.colors = colors

        val dataSet = mutableListOf<IBarDataSet>()
        dataSet.add(set1)

        val barData = BarData(dataSet)
        barData.setValueTextSize(10f)
        barData.barWidth = 0.9f

        bar_chart.data = barData
        bar_chart.invalidate()
    }

    private fun init() {
        val start = 1f
        val values = mutableListOf<BarEntry>()
        for (i in start.toInt() until 110) {
            val value = (Math.random() * (100 + 1)).toFloat()
            values.add(BarEntry(i.toFloat(), value))
        }

        val set1: BarDataSet
        if (bar_chart.data != null && bar_chart.data.dataSetCount > 0) {
            set1 = (bar_chart.data.getDataSetByIndex(0)) as BarDataSet
            set1.values = values
            bar_chart.data.notifyDataChanged()
            bar_chart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "The year 2017")
            set1.setDrawIcons(false)
            set1.colors = RandomColor().randomColor(100).toList()

            val dataSet = mutableListOf<IBarDataSet>()
            dataSet.add(set1)

            val data = BarData(dataSet)
            data.setValueTextSize(10f)
            data.barWidth = 0.9f

            bar_chart.data = data
        }
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        e?.let {
            val bounds = RectF()
            bar_chart.getBarBounds(e as BarEntry, bounds)
            val position = bar_chart.getPosition(e, YAxis.AxisDependency.LEFT)
            MPPointF.recycleInstance(position)
        }
    }
}
