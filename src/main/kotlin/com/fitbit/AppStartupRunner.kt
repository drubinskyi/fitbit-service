package com.fitbit

import com.fitbit.error.exception.WrongCSVHeadersMappingException
import com.fitbit.repository.ActivityRepository
import com.fitbit.util.CSV_FILE_NAME
import com.fitbit.util.parseDailyActivityCSV
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.io.FileNotFoundException
import kotlin.system.exitProcess

@Component
class AppStartupRunner : ApplicationRunner {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var repository: ActivityRepository

    override fun run(args: ApplicationArguments) {
        try {
            val dailyActivitySet = parseDailyActivityCSV(logger, CSV_FILE_NAME)
            repository.saveAll(dailyActivitySet)
            logger.info("{} items added to storage", dailyActivitySet.size)
        }
        catch (e: FileNotFoundException) {
            logger.error("CSV file is not found, closing application")
            exitProcess(1)
        }
        catch (e: WrongCSVHeadersMappingException) {
            logger.error("CSV file headers names are unexpected, closing application")
            exitProcess(1)
        }
    }
}