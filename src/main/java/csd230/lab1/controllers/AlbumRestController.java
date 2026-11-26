package csd230.lab1.controllers;

import csd230.lab1.entities.AlbumEntity;
import csd230.lab1.repositories.AlbumEntityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Album REST API", description = "JSON API for managing musical albums")
@RestController
@RequestMapping("/api/rest/albums")
public class AlbumRestController {
    private final AlbumEntityRepository repository;

    public AlbumRestController(AlbumEntityRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Get all albums")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all albums")
    public List<AlbumEntity> all() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an album")
    @ApiResponse(responseCode = "201", description = "Album created successfully")
    public AlbumEntity newAlbum(@RequestBody AlbumEntity newAlbum) {
        return repository.save(newAlbum);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an album by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the album")
    @ApiResponse(responseCode = "404", description = "Album not found")
    public AlbumEntity one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AlbumNotFoundException(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace an album by ID")
    @ApiResponse(responseCode = "200", description = "Album replaced successfully")
    public AlbumEntity replaceAlbum(@RequestBody AlbumEntity newAlbum, @PathVariable Long id) {
        return repository.findById(id)
                .map(album -> {
                    album.setTitle(newAlbum.getTitle());
                    album.setArtist(newAlbum.getArtist());
                    album.setGenre(newAlbum.getGenre());
                    album.setRecordLabel(newAlbum.getRecordLabel());
                    album.setReleaseYear(newAlbum.getReleaseYear());
                    album.setPrice(newAlbum.getPrice());
                    album.setCopies(newAlbum.getCopies());
                    return repository.save(album);
                })
                .orElseGet(() -> {
                    newAlbum.setId(id);
                    return repository.save(newAlbum);
                });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an album by ID")
    @ApiResponse(responseCode = "204", description = "Album deleted successfully")
    public void deleteAlbum(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new AlbumNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
