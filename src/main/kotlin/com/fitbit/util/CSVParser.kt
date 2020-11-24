package com.fitbit.util

import com.fitbit.error.exception.WrongCSVHeadersMappingException
import com.fitbit.model.DailyActivity
import com.fitbit.validation.areHeaderNamesMapped
import com.fitbit.validation.isCSVValid
import com.fitbit.validation.isOverallTimeValid
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.slf4j.Logger
import org.springframework.core.io.ClassPathResource
import java.io.FileReader

fun parseDailyActivityCSV(logger: Logger, fileName: String): MutableSet<DailyActivity> {
    val activitySet = mutableSetOf<DailyActivity>()

    val csvParser = CSVParser(FileReader(ClassPathResource(fileName).file),
            CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
    )

    var recordsSize = 0

    csvParser.forEach { record ->
        if (!areHeaderNamesMapped(record)) throw WrongCSVHeadersMappingException()
        when {
            isCSVValid(record) -> {
                val dailyActivity = DailyActivity.toDailyActivity(record)
                when {
                    isOverallTimeValid(dailyActivity) -> when {
                        activitySet.contains(dailyActivity) -> logger.info("line with date {} is duplicated", dailyActivity.date)
                        else -> activitySet.add(dailyActivity)
                    }
                    else -> logger.info("line {} is not valid: overall activity time is bigger then possible", record.recordNumber)
                }
            }
            else -> logger.info("line {} is not valid", record.recordNumber)
        }
        recordsSize++
    }
    csvParser.close()
    logger.info("{} from {} records parsed from CSV file", activitySet.size, recordsSize)

    return activitySet
}