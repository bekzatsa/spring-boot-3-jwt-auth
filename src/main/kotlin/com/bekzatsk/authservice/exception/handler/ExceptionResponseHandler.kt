package com.bekzatsk.authservice.exception.handler

import com.bekzatsk.authservice.exception.dto.ExceptionResponseDto
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

@ControllerAdvice
@Slf4j
class ExceptionResponseHandler : ResponseEntityExceptionHandler() {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @ResponseBody
    @ExceptionHandler(ResponseStatusException::class)
    fun responseStatusExceptionHandler(exception: ResponseStatusException): ResponseEntity<*> {
        log.error("Exception occurred: {}", LocalDateTime.now(), exception)
        return ResponseEntity.status(exception.statusCode).body<Any>(ExceptionResponseDto(message = exception.message))
    }

    override fun handleMethodArgumentNotValid(
        exception: MethodArgumentNotValidException,
        headers: HttpHeaders, status: HttpStatusCode, request: WebRequest
    ): ResponseEntity<Any>? {
        val result = exception.bindingResult
        val fieldErrors = result.fieldErrors
        val response = HashMap<Any, Any>()
        response["status"] = "Failure"
        response["message"] =
            fieldErrors.stream().map { fieldError: FieldError -> fieldError.defaultMessage }
                .collect(Collectors.toList())
        response["timestamp"] =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
        return ResponseEntity.badRequest().body(response.toString())
    }

    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    @ExceptionHandler(
        Exception::class
    )
    fun serverExceptionHandler(exception: Exception?): ResponseEntity<*> {
        log.error("Exception occurred: {}", LocalDateTime.now(), exception)
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
            .body<Any>(ExceptionResponseDto(message = "Something went wrong."))
    }
}
