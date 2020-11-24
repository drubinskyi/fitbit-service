package com.fitbit.util

import com.fitbit.error.exception.WrongCSVHeadersMappingException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import java.io.FileNotFoundException

@ExtendWith(MockitoExtension::class)
internal class CSVParserKtTest {
    @Mock
    lateinit var logger: Logger

    @Test
    fun parseDailyActivityCSVWithWrongNameShouldReturnFileNotFound() {
        Assertions.assertThrows(FileNotFoundException::class.java) {
            parseDailyActivityCSV(logger, "invalidName.csv")
        }
    }

    @Test
    fun parseDailyActivityCSV() {
        val dailyActivitySet = parseDailyActivityCSV(logger, "test.csv")

        Assertions.assertEquals(dailyActivitySet.size, 2)
    }

    @Test
    fun parseDailyActivityCSVWithWrongHeaderName() {
        Assertions.assertThrows(WrongCSVHeadersMappingException::class.java) {
            parseDailyActivityCSV(logger, "testWithWrongHeaders.csv")
        }
    }

    @Test
    fun parseDailyActivityCSVWithWrongRecordsSize() {

        val dailyActivitySet = parseDailyActivityCSV(logger, "testWithWrongRecordsSize.csv")

        Assertions.assertEquals(dailyActivitySet.size, 1)
    }
}