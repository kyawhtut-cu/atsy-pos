package com.kyawhtut.pos.utils.color

data class ColorInfo(
    var hueRange: Range?,
    var saturationRange: Range,
    var brightnessRange: Range,
    var lowerBounds: List<Range>
)
