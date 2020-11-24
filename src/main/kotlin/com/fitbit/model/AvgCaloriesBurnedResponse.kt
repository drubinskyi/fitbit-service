package com.fitbit.model

import java.math.BigDecimal
import java.time.LocalDate

data class AvgCaloriesBurnedResponse (
        val startDay: LocalDate,
        val avgCaloriesBurnedPerWeek: BigDecimal
)