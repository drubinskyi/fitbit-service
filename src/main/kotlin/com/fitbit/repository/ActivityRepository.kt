package com.fitbit.repository

import com.fitbit.model.DailyActivity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ActivityRepository : CrudRepository<DailyActivity, LocalDate>