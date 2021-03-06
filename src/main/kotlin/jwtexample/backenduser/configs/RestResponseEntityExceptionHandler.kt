package jwtexample.backenduser.configs

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [RuntimeException::class, IllegalStateException::class])
    protected fun handleConflict(
        ex: RuntimeException?, request: WebRequest?
    ): ResponseEntity<Any> {
        val bodyOfResponse = ex?.message
        return handleExceptionInternal(
            ex!!, bodyOfResponse,
            HttpHeaders(), HttpStatus.BAD_REQUEST, request!!
        )
    }
}