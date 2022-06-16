package com.example.ranierilavastone.Utils

import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {
    companion object {
        fun changedateformat(
            date: String?,
            inputformat: String?,
            outputformat: String?
        ): String? {
            val inputFormat: DateFormat = SimpleDateFormat(inputformat)
            val outputFormat: DateFormat = SimpleDateFormat(outputformat)
            var rawDate: Date? = null
            try {
                rawDate = inputFormat.parse(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return outputFormat.format(rawDate)
        }
    }
}