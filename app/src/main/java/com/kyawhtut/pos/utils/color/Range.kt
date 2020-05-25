package com.kyawhtut.pos.utils.color

class Range(var start: Int, var end: Int) {

    fun contain(value: Int): Boolean {
        return value in start..end
    }

    override fun toString(): String {
        return "start: $start end: $end"
    }
}
