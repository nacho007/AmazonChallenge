package com.idd.amazonchallenge.utils

import android.content.Context
import com.idd.amazonchallenge.R
import java.util.*

object TimeUtils {

    fun entryDateFormatter(context: Context, seconds: Int): String {
        var time = 0L
        var unit = ""

        val dv: Long = seconds.toLong() * 1000
        val df = Date(dv)
        val realSeconds = ((System.currentTimeMillis() - df.time)) / 1000

        if (realSeconds >= 60) {
            time = (realSeconds / 60)
            unit = context.getString(R.string.minute)

            if (time > 1) {
                unit = context.getString(R.string.minutes)
            }
        }

        if (realSeconds >= 3600) {
            time = (realSeconds / 3600)
            unit = context.getString(R.string.hour)

            if (time >= 1) {
                unit = context.getString(R.string.hours)
            }
        }

        if (realSeconds >= 3600 * 24) {
            time = (realSeconds / (3600 * 24))
            unit = context.getString(R.string.day)

            if (time >= 1) {
                unit = context.getString(R.string.days)
            }
        }

        if (realSeconds >= 3600 * 24 * 365) {
            time = (realSeconds / (3600 * 24 * 365))
            unit = context.getString(R.string.year)

            if (time >= 1) {
                unit = context.getString(R.string.years)
            }
        }

        return context.getString(R.string.creation_time_text, time, unit)
    }

}