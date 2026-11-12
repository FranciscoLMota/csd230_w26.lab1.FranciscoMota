package csd230.lab1.controllers;

public class AlbumNotFoundException extends RuntimeException {
    public AlbumNotFoundException(Long id) {
        super("Could not find album with ID: " + id);
    }
}
