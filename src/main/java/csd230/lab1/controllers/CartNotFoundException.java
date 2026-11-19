package csd230.lab1.controllers;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long id) {
        super("Could not find cart " + id);
    }
}
