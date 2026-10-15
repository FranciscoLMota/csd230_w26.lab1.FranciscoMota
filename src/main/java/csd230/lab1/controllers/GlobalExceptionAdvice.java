package csd230.lab1.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler({
            BookNotFoundException.class,
            MagazineNotFoundException.class,
            DiscMagNotFoundException.class,
            TicketNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String entityNotFoundHandler(RuntimeException ex) {
        return ex.getMessage();
    }
}
