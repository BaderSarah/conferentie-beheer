package com.springboot.conferentieapp.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import exceptions.DuplicateLokaalException;
import exceptions.LokaalNotFoundException;

@RestControllerAdvice
class LokaalErrorAdvice {

  @ResponseBody
  @ExceptionHandler(LokaalNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String lokaalNotFoundHandler(LokaalNotFoundException ex) {
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(DuplicateLokaalException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  String duplicateLokaalHandler(DuplicateLokaalException ex) {
    return ex.getMessage();
  }
}
