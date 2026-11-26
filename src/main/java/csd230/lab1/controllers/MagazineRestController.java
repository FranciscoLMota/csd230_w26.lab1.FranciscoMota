package csd230.lab1.controllers;

import csd230.lab1.entities.MagazineEntity;
import csd230.lab1.repositories.MagazineRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Magazine REST API", description = "JSON API for managing magazines")
@RestController
@RequestMapping("/api/rest/magazines")
public class MagazineRestController {
    private final MagazineRepository repository;

    public MagazineRestController(MagazineRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Get all magazines")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all magazines")
    public List<MagazineEntity> all() {
        return repository.findAllMagazines();
    }

    @PostMapping
    @Operation(summary = "Create a magazine")
    @ApiResponse(responseCode = "200", description = "Magazine created successfully")
    public MagazineEntity newMagazine(@RequestBody MagazineEntity newMagazine) {
        return repository.save(newMagazine);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a magazine by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the magazine")
    @ApiResponse(responseCode = "404", description = "Magazine not found")
    public MagazineEntity one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new MagazineNotFoundException(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace a magazine by ID")
    @ApiResponse(responseCode = "200", description = "Magazine replaced successfully")
    public MagazineEntity replaceMagazine(@RequestBody MagazineEntity newMagazine,
                                            @PathVariable Long id) {
        return repository.findById(id)
                .map(magazine -> {
                    magazine.setTitle(newMagazine.getTitle());
                    magazine.setPrice(newMagazine.getPrice());
                    magazine.setCopies(newMagazine.getCopies());
                    magazine.setOrderQty(newMagazine.getOrderQty());
                    magazine.setCurrentIssue(newMagazine.getCurrentIssue());
                    return repository.save(magazine);
                })
                .orElseGet(() -> {
                    newMagazine.setId(id);
                    return repository.save(newMagazine);
                });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a magazine by ID")
    @ApiResponse(responseCode = "200", description = "Magazine deleted successfully")
    public void deleteMagazine(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
