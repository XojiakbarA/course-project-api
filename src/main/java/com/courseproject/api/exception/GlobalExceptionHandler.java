package com.courseproject.api.exception;

import com.courseproject.api.response.RestResponse;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestResponse handleIO(IOException e) {
        RestResponse response = new RestResponse();
        response.setMessage(e.getMessage());
        return response;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse handleBadCredentials(BadCredentialsException e) {
        RestResponse response = new RestResponse();
        response.setMessage("Invalid email and/or password.");
        return response;
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse handleBind(BindException e) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError error : e.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        RestResponse response = new RestResponse();
        response.setMessage("Validation Failed.");
        response.setErrors(errors);
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError error : e.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        RestResponse response = new RestResponse();
        response.setMessage("Validation Failed.");
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

}
