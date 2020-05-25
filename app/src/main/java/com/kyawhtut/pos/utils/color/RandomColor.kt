package com.kyawhtut.pos.utils.color

import java.util.*
import kotlin.math.floor

class RandomColor(private val seed: Long = 0) {

    private val random: Random by lazy {
        Random().apply {
            if (seed != 0L)
                setSeed(seed)
        }
    }
    private val colors by lazy {
        HashMap<String, ColorInfo>()
    }

    init {
        loadColorBounds()
    }

    private fun getColor(hue: Int, saturation: Int, brightness: Int): Int {
        return android.graphics.Color.HSVToColor(
            floatArrayOf(
                hue.toFloat(),
                saturation.toFloat(),
                brightness.toFloat()
            )
        )
    }

    @JvmOverloads
    fun randomColor(
        value: Int = 0,
        saturationType: SaturationType? = null,
        luminosity: Luminosity? = null
    ): Int {
        var hue = value
        hue = pickHue(hue)
        val saturation = pickSaturation(hue, saturationType, luminosity)
        val brightness = pickBrightness(hue, saturation, luminosity)
        return getColor(hue, saturation, brightness)
    }

    fun randomColor(count: Int): IntArray {
        require(count > 0) { "count must be greater than 0" }
        val colors = IntArray(count)
        for (i in 0 until count) {
            colors[i] = randomColor()
        }
        return colors
    }

    fun randomColor(color: Color): Int {
        val hue = pickHue(color.name)
        val saturation = pickSaturation(color, null, null)
        val brightness = pickBrightness(color, saturation, null)
        return getColor(hue, saturation, brightness)
    }

    fun random(color: Color, count: Int): IntArray {
        require(count > 0) { "count must be greater than 0" }
        val colors = IntArray(count)
        for (i in 0 until count) {
            colors[i] = randomColor(color)
        }
        return colors
    }

    private fun pickHue(hue: Int): Int {
        val hueRange = getHueRange(hue)
        return doPickHue(hueRange)
    }

    private fun doPickHue(hueRange: Range): Int {
        var hue = randomWithin(hueRange)

        // Instead of storing red as two seperate ranges,
        // we group them, using negative numbers
        if (hue < 0) {
            hue += 360
        }
        return hue
    }

    private fun pickHue(name: String): Int {
        val hueRange = getHueRange(name)
        return doPickHue(hueRange)
    }

    private fun getHueRange(number: Int): Range {
        return if (number in 1..359) {
            Range(number, number)
        } else Range(0, 360)
    }

    private fun getHueRange(name: String): Range {
        return if (colors.containsKey(name)) {
            colors[name]!!.hueRange!!
        } else Range(0, 360)
    }

    private fun pickSaturation(
        hue: Int,
        saturationType: SaturationType?,
        luminosity: Luminosity?
    ): Int {
        return pickSaturation(getColorInfo(hue), saturationType, luminosity)
    }

    private fun pickSaturation(
        color: Color,
        saturationType: SaturationType?,
        luminosity: Luminosity?
    ): Int {
        val colorInfo = colors[color.name]
        return pickSaturation(colorInfo, saturationType, luminosity)
    }

    private fun pickSaturation(
        colorInfo: ColorInfo?,
        saturationType: SaturationType?,
        luminosity: Luminosity?
    ): Int {
        if (saturationType != null) {
            return when (saturationType) {
                SaturationType.RANDOM -> randomWithin(
                    Range(
                        0,
                        100
                    )
                )
                SaturationType.MONOCHROME -> 0
            }
        }
        if (colorInfo == null) {
            return 0
        }
        val saturationRange = colorInfo.saturationRange
        var min = saturationRange.start
        var max = saturationRange.end
        if (luminosity != null) {
            when (luminosity) {
                Luminosity.LIGHT -> min = 55
                Luminosity.BRIGHT -> min = max - 10
                Luminosity.DARK -> max = 55
            }
        }
        return randomWithin(Range(min, max))
    }

    private fun pickBrightness(hue: Int, saturation: Int, luminosity: Luminosity?): Int {
        val colorInfo = getColorInfo(hue)
        return pickBrightness(colorInfo, saturation, luminosity)
    }

    private fun pickBrightness(
        color: Color,
        saturation: Int,
        luminosity: Luminosity?
    ): Int {
        val colorInfo = colors[color.name]
        return pickBrightness(colorInfo, saturation, luminosity)
    }

    private fun pickBrightness(
        colorInfo: ColorInfo?,
        saturation: Int,
        luminosity: Luminosity?
    ): Int {
        var min = getMinimumBrightness(colorInfo, saturation)
        var max = 100
        if (luminosity != null) {
            when (luminosity) {
                Luminosity.DARK -> max = min + 20
                Luminosity.LIGHT -> min = (max + min) / 2
                Luminosity.RANDOM -> {
                    min = 0
                    max = 100
                }
            }
        }
        return randomWithin(Range(min, max))
    }

    private fun getMinimumBrightness(colorInfo: ColorInfo?, saturation: Int): Int {
        if (colorInfo == null) {
            return 0
        }
        val lowerBounds =
            colorInfo.lowerBounds
        for (i in 0 until lowerBounds.size - 1) {
            val s1 = lowerBounds[i].start
            val v1 = lowerBounds[i].end
            if (i == lowerBounds.size - 1) {
                break
            }
            val s2 = lowerBounds[i + 1].start
            val v2 = lowerBounds[i + 1].end
            if (saturation >= s1 && saturation <= s2) {
                val m = (v2 - v1) / (s2 - s1).toFloat()
                val b = v1 - m * s1
                return (m * saturation + b).toInt()
            }
        }
        return 0
    }

    private fun getColorInfo(tmp: Int): ColorInfo? {
        // Maps red colors to make picking hue easier
        var hue = tmp
        if (hue in 334..360) {
            hue -= 360
        }
        for (key in colors.keys) {
            val colorInfo = colors[key]
            if (colorInfo!!.hueRange != null && colorInfo.hueRange!!.contain(hue)) {
                return colorInfo
            }
        }
        return null
    }

    private fun randomWithin(range: Range): Int {
        return floor(range.start + random.nextDouble() * (range.end + 1 - range.start)).toInt()
    }

    fun defineColor(
        name: String,
        hueRange: Range?,
        lowerBounds: List<Range>
    ) {
        val sMin = lowerBounds[0].start
        val sMax = lowerBounds[lowerBounds.size - 1].start
        val bMin = lowerBounds[lowerBounds.size - 1].end
        val bMax = lowerBounds[0].end
        colors[name] = ColorInfo(
            hueRange,
            Range(sMin, sMax),
            Range(bMin, bMax),
            lowerBounds
        )
    }

    private fun loadColorBounds() {
        val lowerBounds1: MutableList<Range> =
            ArrayList()
        lowerBounds1.add(Range(0, 0))
        lowerBounds1.add(Range(100, 0))
        defineColor(
            Color.MONOCHROME.name,
            null,
            lowerBounds1
        )
        val lowerBounds2: MutableList<Range> =
            ArrayList()
        lowerBounds2.add(Range(20, 100))
        lowerBounds2.add(Range(30, 92))
        lowerBounds2.add(Range(40, 89))
        lowerBounds2.add(Range(50, 85))
        lowerBounds2.add(Range(60, 78))
        lowerBounds2.add(Range(70, 70))
        lowerBounds2.add(Range(80, 60))
        lowerBounds2.add(Range(90, 55))
        lowerBounds2.add(Range(100, 50))
        defineColor(
            Color.RED.name,
            Range(-26, 18),
            lowerBounds2
        )
        val lowerBounds3: MutableList<Range> =
            ArrayList()
        lowerBounds3.add(Range(20, 100))
        lowerBounds3.add(Range(30, 93))
        lowerBounds3.add(Range(40, 88))
        lowerBounds3.add(Range(50, 86))
        lowerBounds3.add(Range(60, 85))
        lowerBounds3.add(Range(70, 70))
        lowerBounds3.add(Range(100, 70))
        defineColor(
            Color.ORANGE.name,
            Range(19, 46),
            lowerBounds3
        )
        val lowerBounds4: MutableList<Range> =
            ArrayList()
        lowerBounds4.add(Range(25, 100))
        lowerBounds4.add(Range(40, 94))
        lowerBounds4.add(Range(50, 89))
        lowerBounds4.add(Range(60, 86))
        lowerBounds4.add(Range(70, 84))
        lowerBounds4.add(Range(80, 82))
        lowerBounds4.add(Range(90, 80))
        lowerBounds4.add(Range(100, 75))
        defineColor(
            Color.YELLOW.name,
            Range(47, 62),
            lowerBounds4
        )
        val lowerBounds5: MutableList<Range> =
            ArrayList()
        lowerBounds5.add(Range(30, 100))
        lowerBounds5.add(Range(40, 90))
        lowerBounds5.add(Range(50, 85))
        lowerBounds5.add(Range(60, 81))
        lowerBounds5.add(Range(70, 74))
        lowerBounds5.add(Range(80, 64))
        lowerBounds5.add(Range(90, 50))
        lowerBounds5.add(Range(100, 40))
        defineColor(
            Color.GREEN.name,
            Range(63, 178),
            lowerBounds5
        )
        val lowerBounds6: MutableList<Range> =
            ArrayList()
        lowerBounds6.add(Range(20, 100))
        lowerBounds6.add(Range(30, 86))
        lowerBounds6.add(Range(40, 80))
        lowerBounds6.add(Range(50, 74))
        lowerBounds6.add(Range(60, 60))
        lowerBounds6.add(Range(70, 52))
        lowerBounds6.add(Range(80, 44))
        lowerBounds6.add(Range(90, 39))
        lowerBounds6.add(Range(100, 35))
        defineColor(
            Color.BLUE.name,
            Range(179, 257),
            lowerBounds6
        )
        val lowerBounds7: MutableList<Range> =
            ArrayList()
        lowerBounds7.add(Range(20, 100))
        lowerBounds7.add(Range(30, 87))
        lowerBounds7.add(Range(40, 79))
        lowerBounds7.add(Range(50, 70))
        lowerBounds7.add(Range(60, 65))
        lowerBounds7.add(Range(70, 59))
        lowerBounds7.add(Range(80, 52))
        lowerBounds7.add(Range(90, 45))
        lowerBounds7.add(Range(100, 42))
        defineColor(
            Color.PURPLE.name,
            Range(258, 282),
            lowerBounds7
        )
        val lowerBounds8: MutableList<Range> =
            ArrayList()
        lowerBounds8.add(Range(20, 100))
        lowerBounds8.add(Range(30, 90))
        lowerBounds8.add(Range(40, 86))
        lowerBounds8.add(Range(60, 84))
        lowerBounds8.add(Range(80, 80))
        lowerBounds8.add(Range(90, 75))
        lowerBounds8.add(Range(100, 73))
        defineColor(
            Color.PINK.name,
            Range(283, 334),
            lowerBounds8
        )
    }

    enum class SaturationType {
        RANDOM, MONOCHROME
    }

    enum class Luminosity {
        BRIGHT, LIGHT, DARK, RANDOM
    }

    enum class Color {
        MONOCHROME, RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, PINK
    }

    class Options {
        var hue = 0
        var saturationType: SaturationType? = null
        var luminosity: Luminosity? = null

    }

    companion object {
        private const val TAG = "RandomColor"
    }
}
