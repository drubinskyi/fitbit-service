package com.fitbit.error

import com.fitbit.error.exception.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(BadRequestException::class)])
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<ErrorResponse> {
        log(ex)
        return ResponseEntity(ErrorResponse(ex.message!!), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(Exception::class)])
    fun handleException(ex: java.lang.Exception): ResponseEntity<ErrorResponse> {
        log(ex)
        return ResponseEntity(ErrorResponse("Something bad happen"), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    fun log(ex: Exception) {
        logger.error("Exception caught:", ex)
    }
}