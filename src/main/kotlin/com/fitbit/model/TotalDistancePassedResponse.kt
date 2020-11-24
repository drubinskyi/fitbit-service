package com.fitbit.model

import java.math.BigDecimal
import java.time.YearMonth

data class TotalDistancePassedResponse (val month: YearMonth,
                                        val totalDistance: BigDecimal
) : Comparable<TotalDistancePassedResponse> {
    override operator fun compareTo(other: TotalDistancePassedResponse) = month.compareTo(other.month)
}