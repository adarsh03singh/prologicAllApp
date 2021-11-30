package com.csestateconnect

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import java.util.regex.Pattern

class DecimalDigitsInputFilter(digitsBeforeZero: Int?, digitsAfterZero: Int?) :
    InputFilter {
    private val mDigitsBeforeZero: Int
    private val mDigitsAfterZero: Int
    private val mPattern: Pattern
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val replacement = source.subSequence(start, end).toString()
        val newVal = (dest.subSequence(0, dstart).toString() + replacement
                + dest.subSequence(dend, dest.length).toString())
        val matcher = mPattern.matcher(newVal)
        if (matcher.matches()) return null
        return if (TextUtils.isEmpty(source)) dest.subSequence(dstart, dend) else ""
    }

    companion object {
        private const val DIGITS_BEFORE_ZERO_DEFAULT = 100
        private const val DIGITS_AFTER_ZERO_DEFAULT = 100
    }

    init {
        mDigitsBeforeZero =
            digitsBeforeZero ?: DecimalDigitsInputFilter.Companion.DIGITS_BEFORE_ZERO_DEFAULT
        mDigitsAfterZero =
            digitsAfterZero ?: DecimalDigitsInputFilter.Companion.DIGITS_AFTER_ZERO_DEFAULT
        mPattern = Pattern.compile(
            "-?[0-9]{0," + mDigitsBeforeZero + "}+((\\.[0-9]{0," + mDigitsAfterZero
                    + "})?)||(\\.)?"
        )
    }
}