package com.fitbit.service

import com.fitbit.error.exception.BadRequestException
import com.fitbit.model.DailyActivityTest.Companion.generateDailyActivity
import com.fitbit.repository.ActivityRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class ActivityServiceTest {

    @Mock
    lateinit var repository: ActivityRepository

    @InjectMocks
    lateinit var service: ActivityService

    @Test
    fun getActivityResponseShouldReturnBadRequestWithNegativeLimitNumber() {
        Assertions.assertThrows(BadRequestException::class.java) {
            service.getActivityResponse(-1, 1)
        }
    }

    @Test
    fun getActivityResponseShouldReturnBadRequestWithNegativePageNumber() {
        Assertions.assertThrows(BadRequestException::class.java) {
            service.getActivityResponse(1, -1)
        }
    }

    @Test
    fun getActivityResponseShouldReturnBadRequestWithZeroPageNumber() {
        Assertions.assertThrows(BadRequestException::class.java) {
            service.getActivityResponse(0, 1)
        }
    }

    @Test
    fun getActivityResponseShouldReturnResourceNotFoundException() {
        `when`(repository.findAll()).thenReturn(listOf(generateDailyActivity(LocalDate.of(2019, 1, 1))))
        val response = service.getActivityResponse(2, 1)

        Assertions.assertEquals(response.result.size, 0)
        Assertions.assertEquals(response.page, 2)
        Assertions.assertEquals(response.totalPages, 1)
    }

    @Test
    fun getActivityResponseShouldReturnAllValuesWhenLimitIsOverThanElementsAmount() {
        `when`(repository.findAll()).thenReturn(listOf(generateDailyActivity(LocalDate.of(2019, 1, 1)),
                generateDailyActivity(LocalDate.of(2019, 1, 2)),
                generateDailyActivity(LocalDate.of(2019, 1, 3))
        ))

        val response = service.getActivityResponse(1, 100)

        Assertions.assertEquals(response.result.size, 3)
        Assertions.assertEquals(response.page, 1)
        Assertions.assertEquals(response.totalPages, 1)
    }

    @Test
    fun getActivityResponseWhenAmountOfRecordsIsZero() {
        `when`(repository.findAll()).thenReturn(emptyList())
        val response = service.getActivityResponse(1, 1)
        Assertions.assertEquals(response.result.size, 0)
        Assertions.assertEquals(response.page, 1)
        Assertions.assertEquals(response.totalPages, 0)
    }

    @Test
    fun getActivityResponse() {
        `when`(repository.findAll()).thenReturn(listOf(generateDailyActivity(LocalDate.of(2019, 1, 1)),
                generateDailyActivity(LocalDate.of(2019, 1, 2)),
                generateDailyActivity(LocalDate.of(2019, 1, 3))
        ))

        val response = service.getActivityResponse(1, 2)

        Assertions.assertEquals(response.result.size, 2)
        Assertions.assertEquals(response.page, 1)
        Assertions.assertEquals(response.totalPages, 2)
    }

    @Test
    fun getAvgCaloriesBurnedShouldReturnBadRequest() {
        Assertions.assertThrows(BadRequestException::class.java) {
            service.getAvgCaloriesBurnedPerWeek(-1)
        }
    }

    @Test
    fun getAvgCaloriesBurnedShouldReturnNoDataFoundExceptionWhenAmountOfRecordsIsZero() {
        `when`(repository.findAll()).thenReturn(emptyList())
        val result = service.getAvgCaloriesBurnedPerWeek(1)
        Assertions.assertEquals(result.size, 0)
    }

    @Test
    fun getAvgCaloriesBurned() {
        val firstDate = LocalDate.of(2019, 1, 1)
        val lastDate = LocalDate.of(2019, 1, 2)
        val dailyActivityList = listOf(generateDailyActivity(firstDate),
                generateDailyActivity(lastDate))

        `when`(repository.findAll()).thenReturn(dailyActivityList)
        `when`(repository.findById(lastDate.minusMonths(3)))
                .thenReturn(Optional.of(generateDailyActivity(lastDate.minusMonths(3))))

        val result = service.getAvgCaloriesBurnedPerWeek(3)

        Assertions.assertEquals(result.size, 1)
        Assertions.assertEquals(result.toList()[0].avgCaloriesBurnedPerWeek, BigDecimal("1.0"))
        Assertions.assertEquals(result.toList()[0].startDay, firstDate)
    }

    @Test
    fun getAvgMinutesActivityShouldReturnBadRequest() {
        Assertions.assertThrows(BadRequestException::class.java) {
            service.getAvgMinutesActivityPerWeek(-1)
        }
    }

    @Test
    fun getAvgMinutesActivityWhenAmountOfRecordsIsZero() {
        `when`(repository.findAll()).thenReturn(emptyList())
        val result = service.getAvgMinutesActivityPerWeek(1)
        Assertions.assertEquals(result.size, 0)
    }

    @Test
    fun getAvgMinutesActivity() {
        val firstDate = LocalDate.of(2019, 1, 1)
        val lastDate = LocalDate.of(2019, 1, 2)
        val dailyActivityList = listOf(generateDailyActivity(firstDate),
                generateDailyActivity(lastDate))

        `when`(repository.findAll()).thenReturn(dailyActivityList)
        `when`(repository.findById(lastDate.minusMonths(2)))
                .thenReturn(Optional.of(generateDailyActivity(lastDate.minusMonths(2))))

        val result = service.getAvgMinutesActivityPerWeek(2)

        Assertions.assertEquals(result.size, 1)
        Assertions.assertEquals(result.toList()[0].avgMinutesActivityPerWeek, BigDecimal("1.0"))
        Assertions.assertEquals(result.toList()[0].startDay, firstDate)
    }

    @Test
    fun getTotalDistancePassedForMonthWhenAmountOfRecordsIsZero() {
        `when`(repository.findAll()).thenReturn(emptyList())
        val result = service.getTotalDistancePassedForMonth()

        Assertions.assertEquals(result.size, 0)
    }


    @Test
    fun getTotalDistancePassedForMonth() {
        val dailyActivityList = listOf(
                generateDailyActivity(LocalDate.of(2019, 1, 1)),
                generateDailyActivity(LocalDate.of(2019, 2, 1)),
                generateDailyActivity(LocalDate.of(2019, 3, 1))
        )

        `when`(repository.findAll()).thenReturn(dailyActivityList)

        val result = service.getTotalDistancePassedForMonth()

        Assertions.assertEquals(result.size, 3)
        Assertions.assertEquals(result.toList()[0].month, YearMonth.of(2019, 1))
        Assertions.assertEquals(result.toList()[0].totalDistance, BigDecimal("1.0"))
        Assertions.assertEquals(result.toList()[1].month, YearMonth.of(2019, 2))
        Assertions.assertEquals(result.toList()[2].month, YearMonth.of(2019, 3))
    }
}