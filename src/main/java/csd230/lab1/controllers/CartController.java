package csd230.lab1.controllers;
import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.ProductEntity;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.ProductEntityRepository;
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


@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartEntityRepository cartRepository;
    @Autowired
    private ProductEntityRepository productRepository;
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

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        CartEntity cart = getCartForCurrentUser(principal);
        model.addAttribute("cart", cart);
        return "cart/cartDetails";
    }

    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, Principal principal) {
        CartEntity cart = getCartForCurrentUser(principal);
        ProductEntity product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            cart.addProduct(product);
            cartRepository.save(cart);
        }
        return "redirect:/";
    }


    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, Principal principal) {
        CartEntity cart = getCartForCurrentUser(principal);
        ProductEntity product = productRepository.findById(productId).orElse(null);

        if (product != null) {
            cart.getProducts().remove(product);
            cartRepository.save(cart);
        }
        return "redirect:/cart";
    }



}


