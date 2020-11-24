package com.fitbit.service

import com.fitbit.error.exception.BadRequestException
import com.fitbit.model.AvgCaloriesBurnedResponse
import com.fitbit.model.AvgMinutesActivityResponse
import com.fitbit.model.PagedActivityResponse
import com.fitbit.model.TotalDistancePassedResponse
import com.fitbit.repository.ActivityRepository
import org.springframework.stereotype.Service
import org.threeten.extra.YearWeek
import java.math.RoundingMode
import java.time.YearMonth


@Service
class ActivityService(
        private val repository: ActivityRepository
) {
    fun getActivityResponse(page: Int, limit: Int): PagedActivityResponse {
        if (limit < 0) {
            throw BadRequestException("Limit can't be negative");
        }

        if (page < 1) {
            throw BadRequestException("Page number can't be less then 1");
        }

        val dailyActivitySet = repository.findAll().toSortedSet()
        val totalSize = dailyActivitySet.size

        val totalPages = if (totalSize % limit == 0) totalSize / limit else totalSize / limit + 1
        when {
            page <= totalPages -> {
                val startIndex = limit * (page - 1)
                val endIndex = minOf(limit * page, totalSize)

                return PagedActivityResponse(dailyActivitySet.toList().subList(startIndex, endIndex),
                        page,
                        totalPages
                )
            }
            else -> return PagedActivityResponse(emptyList(), page, totalPages)
        }

    }

    fun getAvgCaloriesBurnedPerWeek(months: Int): Collection<AvgCaloriesBurnedResponse> {
        if (months < 0) {
            throw BadRequestException("Months can't be negative");
        }

        val dailyActivitySet = repository.findAll().toSortedSet()
        val lastDate = dailyActivitySet.lastOrNull()?.date ?: return emptyList()

        return dailyActivitySet
                .tailSet(repository.findById(lastDate.minusMonths(months.toLong())).get())
                .groupBy { YearWeek.from(it.date) }
                .values
                .filter { it.size == 7 || it.map { da ->  da.date}.contains(lastDate) }
                .map { list ->
                    AvgCaloriesBurnedResponse(list.toSortedSet().first().date,
                            list.map { it.caloriesActivity }.average().toBigDecimal().setScale(1, RoundingMode.HALF_UP))
                }
    }

    fun getAvgMinutesActivityPerWeek(months: Int): Collection<AvgMinutesActivityResponse> {
        if (months < 0) {
            throw BadRequestException("Months can't be negative");
        }

        val dailyActivitySet = repository.findAll().toSortedSet()
        val lastDate = dailyActivitySet.lastOrNull()?.date ?: return emptyList()

        return dailyActivitySet
                .tailSet(repository.findById(lastDate.minusMonths(months.toLong())).get())
                .groupBy { YearWeek.from(it.date) }
                .values
                .filter { it.size == 7 || it.map { da ->  da.date}.contains(lastDate) }
                .map { list ->
                    AvgMinutesActivityResponse(list[0].date,
                            list.map { listOf(it.minutesSlowActivity, it.minutesModerateActivity, it.minutesIntenseActivity).average() }
                                    .average().toBigDecimal().setScale(1, RoundingMode.HALF_UP))
                }
    }

    fun getTotalDistancePassedForMonth(): Collection<TotalDistancePassedResponse> {
        return repository.findAll().toList()
                .groupBy { YearMonth.of(it.date.year, it.date.month) }
                .mapValues { it.value.map { da -> da.distance.toDouble() }.toList().sum().toBigDecimal().setScale(1, RoundingMode.HALF_UP) }
                .map { m -> TotalDistancePassedResponse(m.key, m.value) }.toSortedSet()
    }
}