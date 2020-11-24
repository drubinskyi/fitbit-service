package com.fitbit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.map.repository.config.EnableMapRepositories


@SpringBootApplication
@EnableMapRepositories
class FitbitApplication

fun main(args: Array<String>) {
	runApplication<FitbitApplication>(*args)
}
