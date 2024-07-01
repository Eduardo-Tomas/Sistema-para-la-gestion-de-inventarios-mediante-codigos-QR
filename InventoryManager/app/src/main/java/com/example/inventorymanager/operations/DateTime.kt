package com.example.inventorymanager.operations

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime

class DateTime {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): LocalDate? {
        return LocalDate.now()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): LocalTime? {
        val time = LocalTime.now()
        return LocalTime.of(time.hour, time.minute, time.second)
    }

    // Receives a string in the format yyyy-mm-ddT06:00:00.000Z
    @RequiresApi(Build.VERSION_CODES.O)
    fun normalizeDate(date: String): LocalDate {
        val normalizedDate: String = date.substringBefore("T")

        return LocalDate.parse(normalizedDate)
    }
}