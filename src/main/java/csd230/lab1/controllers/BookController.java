package csd230.lab1.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import csd230.lab1.entities.BookEntity;
import csd230.lab1.entities.CartEntity;
import csd230.lab1.repositories.BookEntityRepository;
import csd230.lab1.repositories.CartEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Books", description = "Operations related to Book Management")
@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookEntityRepository bookRepository;
    @Autowired
    private CartEntityRepository cartRepository;


    @Operation(summary = "Get all books", description = "Returns the HTML view of the book list")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "book/bookList"; // Looks for bookList.html in templates
    }


    @Operation(summary = "Get a book by ID", description = "Returns the HTML view containing the selected book's details")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved book details")
    @GetMapping("/{id}")
    public String getBookById(@PathVariable Long id, Model model) {
        BookEntity book = bookRepository.findById(id).orElse(null);
        model.addAttribute("book", book);
        return "book/bookDetails";
    }


    @Operation(summary = "Show the add book form", description = "Returns the HTML form used to create a book")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the add book form")
    @GetMapping("/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new BookEntity());
        return "book/addBook";
    }


    @Operation(summary = "Add a book", description = "Creates a book and redirects to the book list")
    @ApiResponse(responseCode = "302", description = "Book created; redirecting to the book list")
    @PostMapping("/add")
    public String addBook(@ModelAttribute BookEntity book) {
        bookRepository.save(book);
        return "redirect:/books";
    }


    @Operation(summary = "Show the edit book form", description = "Returns the HTML form used to edit the selected book")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the edit book form")
    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        BookEntity book = bookRepository.findById(id).orElse(null);
        model.addAttribute("book", book);
        return "book/editBook";
    }


    @Operation(summary = "Edit a book", description = "Updates the selected book and redirects to the book list")
    @ApiResponse(responseCode = "302", description = "Book updated; redirecting to the book list")
    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable Long id, @ModelAttribute BookEntity updatedBook) {
        BookEntity existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook != null) {
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setPrice(updatedBook.getPrice());
            existingBook.setCopies(updatedBook.getCopies());
            bookRepository.save(existingBook);
        }
        return "redirect:/books";
    }


    @Operation(summary = "Delete a book", description = "Deletes the selected book and redirects to the book list")
    @ApiResponse(responseCode = "302", description = "Book deleted; redirecting to the book list")
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        BookEntity book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            // Remove this book from any Carts that contain it before deleting
            for (CartEntity cart : book.getCarts()) {
                cart.getProducts().remove(book);
                cartRepository.save(cart);
            }
            bookRepository.deleteById(id);
        }
        return "redirect:/books";
    }
}
