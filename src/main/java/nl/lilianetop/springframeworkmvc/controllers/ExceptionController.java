package nl.lilianetop.springframeworkmvc.controllers;

import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.models.CustomerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ExceptionNotFound.class)
    public ResponseEntity<CustomerDTO> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}
