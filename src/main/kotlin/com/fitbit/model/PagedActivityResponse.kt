package com.fitbit.model

data class PagedActivityResponse(val result: Collection<DailyActivity>,
                                 val page: Int,
                                 val totalPages: Int
)