package com.kyawhtut.pos.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.text.HtmlCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.joda.time.DateTime
import java.util.*

object Extension

fun Boolean.toInt() = if (this) 1 else 0

fun Int.toBoolean() = this == 1

fun getCurrentTimeString(
    format: String = "h:mm a,dd-MM-yyyy",
    locale: Locale = Locale.ENGLISH
): String = DateTime.now().toString(format, locale).replace(",", " at ")

/*Hide Keyboard*/
fun EditText.hideKeyBoard() {
    val imm = this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun Context.isTablet(): Boolean {
    return this.resources.configuration.smallestScreenWidthDp >= 600
}

fun BottomSheetBehavior<*>.isOpen() = this.state == BottomSheetBehavior.STATE_EXPANDED

fun BottomSheetBehavior<*>.isClose() = this.state == BottomSheetBehavior.STATE_COLLAPSED

fun BottomSheetBehavior<*>.open() {
    this.state = BottomSheetBehavior.STATE_EXPANDED
}

fun BottomSheetBehavior<*>.close() {
    this.state = BottomSheetBehavior.STATE_COLLAPSED
}

fun BottomSheetBehavior<*>.hide() {
    this.state = BottomSheetBehavior.STATE_HIDDEN
}

fun String.toHtml() = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)

fun Int.toThousandSeparator(locale: Locale = Locale.getDefault()) =
    String.format(locale, "%,d", this)

fun Double.toThousandSeparator(locale: Locale = Locale.getDefault()) =
    this.toInt().toThousandSeparator()

fun Long.toThousandSeparator(locale: Locale = Locale.getDefault()) =
    String.format(locale, "%,d", this)

fun String.toThousandSeparator(locale: Locale = Locale.getDefault()) =
    String.format(locale, "%,d", this.toLong())

infix fun <T> Collection<T>.deepEqualTo(other: Collection<T>): Boolean {
    // check collections aren't same
    if (this !== other) {
        // fast check of sizes
        if (this.size != other.size) return false
        val areNotEqual = this.asSequence()
            .zip(other.asSequence())
            // check this and other contains same elements at position
            .map { (fromThis, fromOther) -> fromThis == fromOther }
            // searching for first negative answer
            .contains(false)
        if (areNotEqual) return false
    }
    // collections are same or they are contains same elements with same order
    return true
}

infix fun <T> Collection<T>.deepEqualToIgnoreOrder(other: Collection<T>): Boolean {
    // check collections aren't same
    if (this !== other) {
        // fast check of sizes
        if (this.size != other.size) return false
        val areNotEqual = this.asSequence()
            // check other contains next element from this
            .map { it in other }
            // searching for first negative answer
            .contains(false)
        if (areNotEqual) return false
    }
    // collections are same or they are contains same elements
    return true
}
