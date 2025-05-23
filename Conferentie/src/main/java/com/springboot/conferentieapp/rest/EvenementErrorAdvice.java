package com.springboot.conferentieapp.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import exceptions.EvenementNotFoundException;

@RestControllerAdvice
class EvenementErrorAdvice {

  @ResponseBody
  @ExceptionHandler(EvenementNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String evenementNotFoundHandler(EvenementNotFoundException ex) {
    return ex.getMessage();
  }

}
