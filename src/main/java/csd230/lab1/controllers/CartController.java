package csd230.lab1.controllers;
import csd230.lab1.entities.BookEntity;
import csd230.lab1.entities.CartEntity;
import csd230.lab1.repositories.BookEntityRepository;
import csd230.lab1.repositories.CartEntityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;
import csd230.lab1.repositories.UserEntityRepository;
import csd230.lab1.entities.UserEntity;


@Tag(name = "Cart", description = "Operations related to Shopping Cart Management")
@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartEntityRepository cartRepository;
    @Autowired
    private BookEntityRepository bookRepository;
    @Autowired
    private UserEntityRepository userRepository; // Inject this!

    // Helper method to get the current user's cart
    private CartEntity getCartForCurrentUser(Principal principal) {
        // 1. Get username from security context
        String username = principal.getName();
        // 2. Find User in DB
        UserEntity user = userRepository.findByUsername(username);

        // 3. Find Cart for this User
        CartEntity cart = cartRepository.findByUser(user);

        // 4. If no cart exists, create one and link it to the user
        if (cart == null) {
            cart = new CartEntity();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    @Operation(summary = "View the cart", description = "Returns the HTML view of the current user's cart")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the cart")
    @GetMapping
    public String viewCart(Model model, Principal principal) {
        CartEntity cart = getCartForCurrentUser(principal);
        model.addAttribute("cart", cart);
        return "cart/cartDetails";
    }

    @Operation(summary = "Add a book to the cart", description = "Adds the selected book to the current user's cart and redirects to the book list")
    @ApiResponse(responseCode = "302", description = "Book added; redirecting to the book list")
    @GetMapping("/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, Principal principal) {
        CartEntity cart = getCartForCurrentUser(principal);
        BookEntity book = bookRepository.findById(bookId).orElse(null);
        if (book != null) {
            cart.addProduct(book);
            cartRepository.save(cart);
        }
        return "redirect:/books";
    }


    @Operation(summary = "Remove a book from the cart", description = "Removes the selected book from the current user's cart and redirects to the cart")
    @ApiResponse(responseCode = "302", description = "Book removed; redirecting to the cart")
    @GetMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable Long bookId, Principal principal) {
        CartEntity cart = getCartForCurrentUser(principal);
        BookEntity book = bookRepository.findById(bookId).orElse(null);

        if (book != null) {
            cart.getProducts().remove(book);
            cartRepository.save(cart);
        }
        return "redirect:/cart";
    }



}


