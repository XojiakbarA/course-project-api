package com.courseproject.api.exception;

import com.courseproject.api.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    public RestResponse handle(Exception e, Locale locale) {
//        String message = messageSource.getMessage("error.internal", null, locale);
//        RestResponse response = new RestResponse();
//        response.setMessage(message);
//        return response;
//    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public RestResponse handleAuthentication(AccessDeniedException e, Locale locale) {
        String message = messageSource.getMessage("error.forbidden", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse handleBadCredentials(BadCredentialsException e, Locale locale) {
        String message = messageSource.getMessage("error.login", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    @ResponseBody
    public RestResponse handleLocked(LockedException e, Locale locale) {
        String message = messageSource.getMessage("error.locked", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse handleBind(BindException e, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError error : e.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        String message = messageSource.getMessage("error.badRequest", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setErrors(errors);
        return response;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse handleConstraintViolation(ConstraintViolationException e) {
        RestResponse response = new RestResponse();
        response.setMessage(e.getMessage());
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError error : e.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        String message = messageSource.getMessage("error.badRequest", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setErrors(errors);
        return response;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestResponse handleResourceNotFound(ResourceNotFoundException e) {
        RestResponse response = new RestResponse();
        response.setMessage(e.getMessage());
        return response;
    }

    @ExceptionHandler(ResourceExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse handleResourceExists(ResourceExistsException e) {
        RestResponse response = new RestResponse();
        response.setMessage(e.getMessage());
        return response;
    }

    @MessageExceptionHandler(org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException.class)
    @SendTo("/comments/errors")
    public RestResponse handleValid(
            org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException e,
            Locale locale
            ) {
        Map<String, String> errors = new HashMap<>();
        assert e.getBindingResult() != null;
        for(FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        String message = messageSource.getMessage("error.badRequest", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setErrors(errors);
        return response;
    }

}
