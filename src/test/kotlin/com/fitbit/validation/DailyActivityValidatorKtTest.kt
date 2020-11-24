package com.fitbit.validation

import com.fitbit.model.DailyActivity
import com.fitbit.model.DailyActivityTest.Companion.generateDailyActivity
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
internal class DailyActivityValidatorKtTest {

    @Mock
    lateinit var csvRecord: CSVRecord

    @Test
    fun dateRegexShouldBeValid() {
        Assertions.assertTrue(isRecordValid(DATE_REGEX, "08-05-15"))
        Assertions.assertTrue(isRecordValid(DATE_REGEX,("28-02-10")))
        Assertions.assertTrue(isRecordValid(DATE_REGEX,("01-01-99")))
    }

    @Test
    fun dateRegexShouldBeNotValid() {
        Assertions.assertFalse(isRecordValid(DATE_REGEX,("50-05-15")))
        Assertions.assertFalse(isRecordValid(DATE_REGEX,("30-02-10")))
        Assertions.assertFalse(isRecordValid(DATE_REGEX,("31-04-10")))
        Assertions.assertFalse(isRecordValid(DATE_REGEX,("01-18-99")))
        Assertions.assertFalse(isRecordValid(DATE_REGEX,("01-01-1999")))
        Assertions.assertFalse(isRecordValid(DATE_REGEX,("tmp-tmp-1999")))
    }

    @Test
    fun numberWithSeparatorShouldBeValid() {
        Assertions.assertTrue(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, "1.123"))
        Assertions.assertTrue(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, "1.12"))
        Assertions.assertTrue(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, "1.1"))
        Assertions.assertTrue(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, "123.112.1"))
        Assertions.assertTrue(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, "123"))
        Assertions.assertTrue(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, "0"))
    }

    @Test
    fun numberWithSeparatorShouldBeNotValid() {
        Assertions.assertFalse(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, "1.12.3"))
        Assertions.assertFalse(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, "1.123.12.21"))
        Assertions.assertFalse(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, "1.1."))
        Assertions.assertFalse(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, "0.123"))
        Assertions.assertFalse(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, ".123"))
        Assertions.assertFalse(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, "-123"))
        Assertions.assertFalse(isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, "tmp"))
    }

    @Test
    fun intShouldBeValid() {
        Assertions.assertTrue(isRecordValid(POSITIVE_INT_REGEX, "1"))
        Assertions.assertTrue(isRecordValid(POSITIVE_INT_REGEX, "0"))
        Assertions.assertTrue(isRecordValid(POSITIVE_INT_REGEX, "12345"))
    }

    @Test
    fun intShouldBeNotValid() {
        Assertions.assertFalse(isRecordValid(POSITIVE_INT_REGEX, "-2"))
        Assertions.assertFalse(isRecordValid(POSITIVE_INT_REGEX, "01"))
        Assertions.assertFalse(isRecordValid(POSITIVE_INT_REGEX, "1wr3"))
    }

    @Test
    fun rationalShouldBaValid() {
        Assertions.assertTrue(isRecordValid(POSITIVE_RATIONAL_REGEX, "1,2234"))
        Assertions.assertTrue(isRecordValid(POSITIVE_RATIONAL_REGEX, "0,2"))
        Assertions.assertTrue(isRecordValid(POSITIVE_RATIONAL_REGEX, "1"))
        Assertions.assertTrue(isRecordValid(POSITIVE_RATIONAL_REGEX, "0"))
    }

    @Test
    fun rationalShouldBaNotValid() {
        Assertions.assertFalse(isRecordValid(POSITIVE_RATIONAL_REGEX, "1,2,3"))
        Assertions.assertFalse(isRecordValid(POSITIVE_RATIONAL_REGEX, "-1,2234"))
        Assertions.assertFalse(isRecordValid(POSITIVE_RATIONAL_REGEX, "1,ert"))
        Assertions.assertFalse(isRecordValid(POSITIVE_RATIONAL_REGEX, "1,"))
        Assertions.assertFalse(isRecordValid(POSITIVE_RATIONAL_REGEX, "01,123"))
    }

    @Test
    fun shouldReturnOverallTimeValid() {
        Assertions.assertTrue(isOverallTimeValid(generateDailyActivity(LocalDate.of(2019, 1, 1))))
    }

    @Test
    fun shouldReturnOverallTimeIsNotValid() {
        val dailyActivity = DailyActivity(
                LocalDate.of(2019, 1, 1),
                1,
                1,
                BigDecimal.ONE,
                1,
                1440,
                1,
                1,
                1,
                1
        )
        Assertions.assertFalse(isOverallTimeValid(dailyActivity))
    }

    @Test
    fun isCSVValid() {
        Mockito.`when`(csvRecord.isConsistent).thenReturn(true)
        Mockito.`when`(csvRecord.get(DATE_HEADER)).thenReturn("08-05-15")
        Mockito.`when`(csvRecord.get(CALORIES_HEADER)).thenReturn("3.631")
        Mockito.`when`(csvRecord.get(STEPS_HEADER)).thenReturn("18.925")
        Mockito.`when`(csvRecord.get(DISTANCE_HEADER)).thenReturn("14,11")
        Mockito.`when`(csvRecord.get(FLOORS_HEADER)).thenReturn("4")
        Mockito.`when`(csvRecord.get(MINUTES_SITTING_HEADER)).thenReturn("1")
        Mockito.`when`(csvRecord.get(MINUTES_SLOW_ACTIVITY_HEADER)).thenReturn("1")
        Mockito.`when`(csvRecord.get(MINUTES_MODERATE_ACTIVITY_HEADER)).thenReturn("1")
        Mockito.`when`(csvRecord.get(MINUTES_INTENSE_ACTIVITY_HEADER)).thenReturn("1")
        Mockito.`when`(csvRecord.get(CALORIES_ACTIVITY_HEADER)).thenReturn("2.248")

        Assertions.assertTrue(isCSVValid(csvRecord))
    }

    @Test
    fun areHeadersMapped() {
        Mockito.`when`(csvRecord.isMapped(DATE_HEADER)).thenReturn(true)
        Mockito.`when`(csvRecord.isMapped(CALORIES_HEADER)).thenReturn(true)
        Mockito.`when`(csvRecord.isMapped(STEPS_HEADER)).thenReturn(true)
        Mockito.`when`(csvRecord.isMapped(DISTANCE_HEADER)).thenReturn(true)
        Mockito.`when`(csvRecord.isMapped(FLOORS_HEADER)).thenReturn(true)
        Mockito.`when`(csvRecord.isMapped(MINUTES_SITTING_HEADER)).thenReturn(true)
        Mockito.`when`(csvRecord.isMapped(MINUTES_SLOW_ACTIVITY_HEADER)).thenReturn(true)
        Mockito.`when`(csvRecord.isMapped(MINUTES_MODERATE_ACTIVITY_HEADER)).thenReturn(true)
        Mockito.`when`(csvRecord.isMapped(MINUTES_INTENSE_ACTIVITY_HEADER)).thenReturn(true)
        Mockito.`when`(csvRecord.isMapped(CALORIES_ACTIVITY_HEADER)).thenReturn(true)

        Assertions.assertTrue(areHeaderNamesMapped(csvRecord))
    }

}