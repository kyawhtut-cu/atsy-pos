package com.kyawhtut.pos.data.vo

data class FunctionVO private constructor(
    val functionTitle: String,
    val functionDescription: String,
    val functionAvailable: Boolean,
    var status: Boolean
) {
    class Builder {
        var functionTitle: String = ""
        var functionDescription: String = ""
        var functionAvailable: Boolean = false
        var status: Boolean = false

        fun build() = FunctionVO(functionTitle, functionDescription, functionAvailable, status)
    }
}

class FunctionList : ArrayList<FunctionVO>() {
    fun function(block: FunctionVO.Builder.() -> Unit) {
        add(FunctionVO.Builder().apply(block).build())
    }
}

fun functionList(block: FunctionList.() -> Unit) = FunctionList().apply(block)

fun function(block: FunctionVO.Builder.() -> Unit) = FunctionVO.Builder().apply(block).build()