package com.fitbit.controller

import com.fitbit.model.AvgCaloriesBurnedResponse
import com.fitbit.model.AvgMinutesActivityResponse
import com.fitbit.model.PagedActivityResponse
import com.fitbit.model.TotalDistancePassedResponse
import com.fitbit.service.ActivityService
import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(value = "activity", description = "Activity API")
@RequestMapping("/api/activity")
class ActivityController(
        private val service: ActivityService
) {
    @GetMapping
    fun getActivity(@RequestParam(value = "page") page: Int,
                    @RequestParam(value = "limit", required = false, defaultValue = "20") size: Int): ResponseEntity<PagedActivityResponse> {
        return ResponseEntity.ok(service.getActivityResponse(page, size))
    }

    @GetMapping("/average_calories")
    fun getAvgCalories(@RequestParam(value = "months", required = false, defaultValue = "3") months: Int):
            ResponseEntity<Collection<AvgCaloriesBurnedResponse>> {
        return ResponseEntity.ok(service.getAvgCaloriesBurnedPerWeek(months))
    }

    @GetMapping("/average_minutes_activity")
    fun getAvgMinutesActivity(@RequestParam(value = "months", required = false, defaultValue = "2") months: Int):
            ResponseEntity<Collection<AvgMinutesActivityResponse>> {
        return ResponseEntity.ok(service.getAvgMinutesActivityPerWeek(months))
    }

    @GetMapping("/total_distance")
    fun getTotalDistancePassedForMonth(): ResponseEntity<Collection<TotalDistancePassedResponse>> {
        return ResponseEntity.ok(service.getTotalDistancePassedForMonth())
    }

}