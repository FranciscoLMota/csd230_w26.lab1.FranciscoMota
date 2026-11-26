package csd230.lab1.controllers;

import csd230.lab1.entities.DiscMagEntity;
import csd230.lab1.repositories.DiscMagRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "DiscMag REST API", description = "JSON API for managing disc magazines")
@RestController
@RequestMapping("/api/rest/discmags")
public class DiscMagRestController {
    private final DiscMagRepository repository;

    public DiscMagRestController(DiscMagRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Get all disc magazines")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all disc magazines")
    public List<DiscMagEntity> all() {
        return repository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a disc magazine")
    @ApiResponse(responseCode = "200", description = "Disc magazine created successfully")
    public DiscMagEntity newDiscMag(@RequestBody DiscMagEntity newDiscMag) {
        return repository.save(newDiscMag);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a disc magazine by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the disc magazine")
    @ApiResponse(responseCode = "404", description = "Disc magazine not found")
    public DiscMagEntity one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DiscMagNotFoundException(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace a disc magazine by ID")
    @ApiResponse(responseCode = "200", description = "Disc magazine replaced successfully")
    public DiscMagEntity replaceDiscMag(@RequestBody DiscMagEntity newDiscMag,
                                         @PathVariable Long id) {
        return repository.findById(id)
                .map(discMag -> {
                    discMag.setTitle(newDiscMag.getTitle());
                    discMag.setPrice(newDiscMag.getPrice());
                    discMag.setCopies(newDiscMag.getCopies());
                    discMag.setOrderQty(newDiscMag.getOrderQty());
                    discMag.setCurrentIssue(newDiscMag.getCurrentIssue());
                    discMag.setHasDisc(newDiscMag.isHasDisc());
                    return repository.save(discMag);
                })
                .orElseGet(() -> {
                    newDiscMag.setId(id);
                    return repository.save(newDiscMag);
                });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a disc magazine by ID")
    @ApiResponse(responseCode = "200", description = "Disc magazine deleted successfully")
    public void deleteDiscMag(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
