package ru.sruit.vultusservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.sruit.vultusservice.models.exception.ApiValidationException;
import ru.sruit.vultusservice.models.response.contoller.Response;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ApiValidationException.class)
    public <T> Response<T> handleConflict(ApiValidationException ex) {
        return Response.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public <T> Response<T> onConstraintValidationException(ConstraintViolationException e) {
        final ConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();
        return Response.error(400, violation.getMessage());
    }
}
