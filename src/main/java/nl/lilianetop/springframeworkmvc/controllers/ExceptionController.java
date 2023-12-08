package nl.lilianetop.springframeworkmvc.controllers;

import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.models.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ExceptionNotFound.class)
    public ResponseEntity<Customer> handleNotFoundException(){
        return ResponseEntity.notFound().build();
    }
}
