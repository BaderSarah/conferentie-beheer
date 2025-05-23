package com.springboot.conferentieapp.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import exceptions.DuplicateSprekerException;
import exceptions.SprekerNotFoundException;

@RestControllerAdvice
class SprekerErrorAdvice {

  @ResponseBody
  @ExceptionHandler(SprekerNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String sprekerNotFoundHandler(SprekerNotFoundException ex) {
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(DuplicateSprekerException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  String duplicateSprekerHandler(DuplicateSprekerException ex) {
    return ex.getMessage();
  }
}
