package com.fitbit.model

import com.fitbit.util.*
import org.apache.commons.csv.CSVRecord
import org.springframework.data.annotation.Id
import org.springframework.data.keyvalue.annotation.KeySpace
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@KeySpace("activities")
data class DailyActivity (
        @Id
        val date: LocalDate,
        val calories: Int,
        val steps: Int,
        val distance: BigDecimal,
        val floors: Int,
        val minutesSitting: Int,
        val minutesSlowActivity: Int,
        val minutesModerateActivity: Int,
        val minutesIntenseActivity: Int,
        val caloriesActivity: Int) : Comparable<DailyActivity> {

    override operator fun compareTo(other: DailyActivity) = date.compareTo(other.date)

    companion object {
        @JvmStatic
        fun toDailyActivity(record: CSVRecord): DailyActivity {
            val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

            return DailyActivity(
                    LocalDate.parse(record.get(DATE_HEADER), dateFormatter),
                    formatNumberWithSeparator(record.get(CALORIES_HEADER)).toInt(),
                    formatNumberWithSeparator(record.get(STEPS_HEADER)).toInt(),
                    formatRational(record.get(DISTANCE_HEADER)).toBigDecimal(),
                    record.get(FLOORS_HEADER).toInt(),
                    formatNumberWithSeparator(record.get(MINUTES_SITTING_HEADER)).toInt(),
                    record.get(MINUTES_SLOW_ACTIVITY_HEADER).toInt(),
                    record.get(MINUTES_MODERATE_ACTIVITY_HEADER).toInt(),
                    record.get(MINUTES_INTENSE_ACTIVITY_HEADER).toInt(),
                    formatNumberWithSeparator(record.get(CALORIES_ACTIVITY_HEADER)).toInt()
            )
        }

        @JvmStatic
        fun formatNumberWithSeparator(input: String): String {
            return input
                    .replaceAfterLast('.', input.substringAfterLast('.').padEnd(3, '0'))
                    .replace(".", "")
        }

        @JvmStatic
        fun formatRational(input: String): String {
            return input.replace(',', '.')
        }
    }
}