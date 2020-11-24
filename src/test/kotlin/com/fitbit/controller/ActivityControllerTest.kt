package com.fitbit.controller

import com.fitbit.model.AvgCaloriesBurnedResponse
import com.fitbit.model.AvgMinutesActivityResponse
import com.fitbit.model.DailyActivityTest.Companion.generateDailyActivity
import com.fitbit.model.PagedActivityResponse
import com.fitbit.model.TotalDistancePassedResponse
import com.fitbit.service.ActivityService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

@ExtendWith(MockitoExtension::class)
internal class ActivityControllerTest {

    @Mock
    lateinit var service: ActivityService

    @InjectMocks
    lateinit var controller: ActivityController

    @Test
    fun getActivity() {
        val pagedActivityResponse = PagedActivityResponse(listOf(generateDailyActivity(LocalDate.of(2019, 1, 1))), 1, 1)
        `when`(service.getActivityResponse(Mockito.anyInt(), Mockito.anyInt())).thenReturn(pagedActivityResponse)

        val result = controller.getActivity(1, 1)
        Assertions.assertEquals(result.body, pagedActivityResponse)
        Assertions.assertEquals(result.statusCode, HttpStatus.OK)
    }

    @Test
    fun getAvgCalories() {
        val avgCaloriesBurnedResponse = AvgCaloriesBurnedResponse(LocalDate.of(2019, 1, 1), BigDecimal.ONE)
        `when`(service.getAvgCaloriesBurnedPerWeek(Mockito.anyInt())).thenReturn(listOf(avgCaloriesBurnedResponse))

        val result = controller.getAvgCalories(1)
        Assertions.assertTrue(result.body!!.contains(avgCaloriesBurnedResponse))
        Assertions.assertEquals(result.statusCode, HttpStatus.OK)
    }

    @Test
    fun getAvgMinutesActivity() {
        val avgMinutesActivityResponse = AvgMinutesActivityResponse(LocalDate.of(2019, 1, 1), BigDecimal.ONE)

        `when`(service.getAvgMinutesActivityPerWeek(Mockito.anyInt())).thenReturn(listOf(avgMinutesActivityResponse))

        val result = controller.getAvgMinutesActivity(1)
        Assertions.assertTrue(result.body!!.contains(avgMinutesActivityResponse))
        Assertions.assertEquals(result.statusCode, HttpStatus.OK)
    }

    @Test
    fun getTotalDistancePassedForMonth() {
        val totalDistancePassedResponse = TotalDistancePassedResponse(YearMonth.of(2019,1), BigDecimal.ONE)

        `when`(service.getTotalDistancePassedForMonth()).thenReturn(listOf(totalDistancePassedResponse))

        val result = controller.getTotalDistancePassedForMonth()
        Assertions.assertTrue(result.body!!.contains(totalDistancePassedResponse))
        Assertions.assertEquals(result.statusCode, HttpStatus.OK)
    }

}