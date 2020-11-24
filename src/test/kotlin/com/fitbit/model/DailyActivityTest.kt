package com.fitbit.model

import com.fitbit.model.DailyActivity.Companion.formatNumberWithSeparator
import com.fitbit.model.DailyActivity.Companion.formatRational
import com.fitbit.model.DailyActivity.Companion.toDailyActivity
import com.fitbit.util.*
import org.apache.commons.csv.CSVRecord
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class DailyActivityTest {

    @Mock
    lateinit var csvRecord: CSVRecord

    @Test
    fun compareTo() {
        val dailyActivity1 = generateDailyActivity(LocalDate.of(2019, 1, 1))
        val dailyActivity2 = generateDailyActivity(LocalDate.of(2019, 1, 1))
        val dailyActivity3 = generateDailyActivity(LocalDate.of(2019, 1, 2))
        val dailyActivity4 = generateDailyActivity(LocalDate.of(2020, 1, 2))

        Assertions.assertEquals(dailyActivity1.compareTo(dailyActivity2), 0)
        Assertions.assertEquals(dailyActivity1.compareTo(dailyActivity3), -1)
        Assertions.assertEquals(dailyActivity4.compareTo(dailyActivity3), 1)
    }

    @Test
    fun toDailyActivityStaticInitializer() {
        Mockito.`when`(csvRecord.get(DATE_HEADER)).thenReturn("08-05-15")
        Mockito.`when`(csvRecord.get(CALORIES_HEADER)).thenReturn("3.631")
        Mockito.`when`(csvRecord.get(STEPS_HEADER)).thenReturn("18.925")
        Mockito.`when`(csvRecord.get(DISTANCE_HEADER)).thenReturn("14,11")
        Mockito.`when`(csvRecord.get(FLOORS_HEADER)).thenReturn("4")
        Mockito.`when`(csvRecord.get(MINUTES_SITTING_HEADER)).thenReturn("101")
        Mockito.`when`(csvRecord.get(MINUTES_SLOW_ACTIVITY_HEADER)).thenReturn("22")
        Mockito.`when`(csvRecord.get(MINUTES_MODERATE_ACTIVITY_HEADER)).thenReturn("30")
        Mockito.`when`(csvRecord.get(MINUTES_INTENSE_ACTIVITY_HEADER)).thenReturn("14")
        Mockito.`when`(csvRecord.get(CALORIES_ACTIVITY_HEADER)).thenReturn("2.248")

        val dailyActivity = toDailyActivity(csvRecord)

        Assertions.assertEquals(dailyActivity.date, LocalDate.of(2015, 5, 8))
        Assertions.assertEquals(dailyActivity.calories, 3631)
        Assertions.assertEquals(dailyActivity.steps, 18925)
        Assertions.assertEquals(dailyActivity.distance, BigDecimal("14.11"))
        Assertions.assertEquals(dailyActivity.floors, 4)
        Assertions.assertEquals(dailyActivity.minutesSitting, 101)
        Assertions.assertEquals(dailyActivity.minutesSlowActivity, 22)
        Assertions.assertEquals(dailyActivity.minutesModerateActivity, 30)
        Assertions.assertEquals(dailyActivity.minutesIntenseActivity, 14)
        Assertions.assertEquals(dailyActivity.caloriesActivity, 2248)
    }

    @Test
    fun numberWithSeparatorFormatting() {
        val numberInput = "123.200.23"
        val numberOutput = "123200230"

        Assertions.assertEquals(formatNumberWithSeparator(numberInput), numberOutput)
    }

    @Test
    fun rationalFormatting() {
        val numberInput = "1,2"
        val numberOutput = "1.2"

        Assertions.assertEquals(formatRational(numberInput), numberOutput)
    }

    companion object {
        @JvmStatic
        fun generateDailyActivity(date: LocalDate): DailyActivity {
            return DailyActivity(
                    date,
                    1,
                    1,
                    BigDecimal.ONE,
                    1,
                    1,
                    1,
                    1,
                    1,
                    1
            )
        }
    }
}