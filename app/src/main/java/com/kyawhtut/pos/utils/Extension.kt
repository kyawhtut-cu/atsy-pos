package com.kyawhtut.pos.utils

import org.joda.time.DateTime
import java.util.*

object Extension

fun Boolean.toInt() = if (this) 1 else 0

fun Int.toBoolean() = this == 1

fun getCurrentTimeString(format: String = "dd-MM-yyyy", locale: Locale = Locale.ENGLISH): String =
    DateTime.now().toString(format, locale)