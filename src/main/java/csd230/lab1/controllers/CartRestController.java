package csd230.lab1.controllers;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.ProductEntityRepository;
import csd230.lab1.repositories.UserEntityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.entities.UserEntity;
import csd230.lab1.repositories.UserEntityRepository;
import csd230.lab1.repositories.ProductEntityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;


import java.util.List;

@Tag(name = "Carts", description = "JSON API for managing carts")
@RestController
@RequestMapping("/api/rest/carts")
public class CartRestController {
    private final CartEntityRepository repository;
    private final UserEntityRepository userRepository;
    private final ProductEntityRepository productRepository;

    public CartRestController(CartEntityRepository repository,
                              UserEntityRepository userRepository,
                              ProductEntityRepository productRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    @GetMapping
    @Operation(summary = "List carts")
    @ApiResponse(responseCode = "200", description = "Carts returned")
    public List<CartEntity> all() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a cart")
    @ApiResponse(responseCode = "200", description = "Cart returned")
    @ApiResponse(responseCode = "404", description = "Cart not found")
    public CartEntity one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CartNotFoundException(id));
    }

    @PostMapping
    @Operation(summary = "Create a cart")
    @ApiResponse(responseCode = "200", description = "Cart created")
    public CartEntity create(@RequestBody CartEntity cart) {
        return repository.save(cart);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace a cart")
    @ApiResponse(responseCode = "200", description = "Cart replaced")
    @ApiResponse(responseCode = "404", description = "Cart not found")
    public CartEntity replace(@RequestBody CartEntity replacement, @PathVariable Long id) {
        CartEntity cart = repository.findById(id)
                .orElseThrow(() -> new CartNotFoundException(id));
        cart.setProducts(replacement.getProducts());
        cart.setUser(replacement.getUser());
        return repository.save(cart);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a cart")
    @ApiResponse(responseCode = "200", description = "Cart deleted")
    @ApiResponse(responseCode = "404", description = "Cart not found")
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new CartNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @PostMapping("/mine/products/{productId}")
    @Transactional
    @Operation(summary = "Add an existing product to the signed-in user's cart")
    public CartEntity addMine(@PathVariable Long productId, Principal principal) {
        var user = currentUser(principal, true);
        var product = productRepository.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        CartEntity cart = repository.findByUser(user);
        if (cart == null) {
            cart = new CartEntity();
            cart.setUser(user);
        }
        cart.addProduct(product);
        return repository.save(cart);
    }

    // The authenticated username is the owner; no client cart/user ID is accepted.
    private UserEntity currentUser(Principal principal, boolean forUpdate) {
        var user = principal == null ? null : forUpdate
                                              ? userRepository.findForCartUpdate(principal.getName())
                                              : userRepository.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED);
        }
        return user;
    }


    @GetMapping("/mine")
    @Transactional(readOnly = true)
    @Operation(summary = "Read the signed-in user's cart; 204 if none has been created")
    public ResponseEntity<CartEntity> mine(Principal principal) {
        CartEntity cart = repository.findByUser(currentUser(principal, false));
        return cart == null ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(cart);
    }

    @DeleteMapping("/mine/products/{productId}")
    @Transactional
    @Operation(summary = "Remove a product from the signed-in user's cart")
    public ResponseEntity<CartEntity> removeMine(
            @PathVariable Long productId, Principal principal) {
        var user = currentUser(principal, true);
        var product = productRepository.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        CartEntity cart = repository.findByUser(user);
        if (cart == null) {
            return ResponseEntity.noContent().build();
        }
        cart.getProducts().remove(product);
        product.getCarts().remove(cart);
        return ResponseEntity.ok(cart);
    }



}
