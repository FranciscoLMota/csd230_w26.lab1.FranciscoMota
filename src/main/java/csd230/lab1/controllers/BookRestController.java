package csd230.lab1.controllers;

import csd230.lab1.entities.BookEntity;
import csd230.lab1.repositories.BookEntityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Book REST API", description = "JSON API for managing books")
@RestController
@RequestMapping("/api/rest/books")
public class BookRestController {
    private final BookEntityRepository repository;

    public BookRestController(BookEntityRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Get all books")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all books")
    public List<BookEntity> all() {
        return repository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a book")
    @ApiResponse(responseCode = "200", description = "Book created successfully")
    public BookEntity newBook(@RequestBody BookEntity newBook) {
        return repository.save(newBook);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the book")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public BookEntity one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace a book by ID")
    @ApiResponse(responseCode = "200", description = "Book replaced successfully")
    public BookEntity replaceBook(@RequestBody BookEntity newBook, @PathVariable Long id) {
        return repository.findById(id)
                .map(book -> {
                    book.setTitle(newBook.getTitle());
                    book.setAuthor(newBook.getAuthor());
                    book.setPrice(newBook.getPrice());
                    book.setCopies(newBook.getCopies());
                    return repository.save(book);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return repository.save(newBook);
                });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book by ID")
    @ApiResponse(responseCode = "200", description = "Book deleted successfully")
    public void deleteBook(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
