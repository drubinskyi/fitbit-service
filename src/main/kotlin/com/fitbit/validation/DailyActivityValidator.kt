package com.fitbit.validation

import com.fitbit.model.DailyActivity
import com.fitbit.util.*
import org.apache.commons.csv.CSVRecord

fun isCSVValid(record: CSVRecord)= record.isConsistent &&
            isRecordValid(DATE_REGEX, record.get(DATE_HEADER)) &&
            isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, record.get(CALORIES_HEADER)) &&
            isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, record.get(STEPS_HEADER)) &&
            isRecordValid(POSITIVE_RATIONAL_REGEX, record.get(DISTANCE_HEADER)) &&
            isRecordValid(POSITIVE_INT_REGEX,record.get(FLOORS_HEADER)) &&
            isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, record.get(MINUTES_SITTING_HEADER)) &&
            isRecordValid(POSITIVE_INT_REGEX, record.get(MINUTES_SLOW_ACTIVITY_HEADER)) &&
            isRecordValid(POSITIVE_INT_REGEX, record.get(MINUTES_MODERATE_ACTIVITY_HEADER)) &&
            isRecordValid(POSITIVE_INT_REGEX, record.get(MINUTES_INTENSE_ACTIVITY_HEADER)) &&
            isRecordValid(POSITIVE_NUMBER_WITH_SEPARATOR_REGEX, record.get(CALORIES_ACTIVITY_HEADER))

fun isRecordValid(regex: String, record: String) = regex.toRegex().matches(record)

fun isOverallTimeValid(activity: DailyActivity) =
        activity.minutesIntenseActivity + activity.minutesModerateActivity + activity.minutesSlowActivity + activity.minutesSitting <= MINUTES_IN_DAY

fun areHeaderNamesMapped(record: CSVRecord) = record.isMapped(DATE_HEADER) &&
        record.isMapped(CALORIES_HEADER) &&
        record.isMapped(STEPS_HEADER) &&
        record.isMapped(DISTANCE_HEADER) &&
        record.isMapped(FLOORS_HEADER) &&
        record.isMapped(MINUTES_SITTING_HEADER) &&
        record.isMapped(MINUTES_SLOW_ACTIVITY_HEADER) &&
        record.isMapped(MINUTES_MODERATE_ACTIVITY_HEADER) &&
        record.isMapped(MINUTES_INTENSE_ACTIVITY_HEADER) &&
        record.isMapped(CALORIES_ACTIVITY_HEADER)