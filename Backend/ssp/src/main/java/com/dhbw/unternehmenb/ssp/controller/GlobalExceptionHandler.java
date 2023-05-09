package com.dhbw.unternehmenb.ssp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.time.LocalTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<String> handleException (Exception ex, HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();

        logger.error("Error in : " + request.getRequestURI());
        logger.error(ex.getMessage(), ex);

        String timestamp = LocalDate.now() + "T" + LocalTime.now();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(ex.getMessage(), headers, status);
    }
}
