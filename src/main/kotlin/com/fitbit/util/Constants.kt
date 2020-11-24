package com.fitbit.util

const val CSV_FILE_NAME = "One_Year_of_FitBitChargeHR_Data.csv"

const val DATE_REGEX = "^(((0[1-9]|[1-2][0-9]|3[0-1])-(0[13578]|(10|12)))|((0[1-9]|[1-2][0-9])-02)|((0[1-9]|[1-2][0-9]|30))-(0[469]|11))-[0-9]{2}\$"
const val POSITIVE_NUMBER_WITH_SEPARATOR_REGEX = "^(?:0|[1-9][0-9]{0,2}(?:\\.[0-9]{3})*(\\.[0-9]{1,3})?)\$"
const val POSITIVE_INT_REGEX = "^0|[1-9][0-9]*\$"
const val POSITIVE_RATIONAL_REGEX ="^(0|[1-9][0-9]*)(,[0-9]+)?$"
const val DATE_PATTERN = "dd-MM-yy"

const val DATE_HEADER = "Date"
const val CALORIES_HEADER = "Calories"
const val STEPS_HEADER = "Steps"
const val DISTANCE_HEADER = "Distance"
const val FLOORS_HEADER = "floors"
const val MINUTES_SITTING_HEADER = "Minutes_sitting"
const val MINUTES_SLOW_ACTIVITY_HEADER = "Minutes_of_slow_activity"
const val MINUTES_MODERATE_ACTIVITY_HEADER = "Minutes_of_moderate_activity"
const val MINUTES_INTENSE_ACTIVITY_HEADER = "Minutes_of_intense_activity"
const val CALORIES_ACTIVITY_HEADER = "Calories_Activity"

const val MINUTES_IN_DAY = 1440
