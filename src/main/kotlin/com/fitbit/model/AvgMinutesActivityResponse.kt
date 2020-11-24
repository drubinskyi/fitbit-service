package com.fitbit.model

import java.math.BigDecimal
import java.time.LocalDate

data class AvgMinutesActivityResponse (
        val startDay: LocalDate,
        val avgMinutesActivityPerWeek: BigDecimal
)