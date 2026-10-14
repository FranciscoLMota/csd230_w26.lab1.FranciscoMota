package csd230.lab1.controllers;

import csd230.lab1.entities.*;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.OrderEntityRepository;
import csd230.lab1.repositories.ProductEntityRepository;
import csd230.lab1.repositories.UserEntityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Orders", description = "Operations related to Order Management")
@Controller
public class OrderController {

    private final CartEntityRepository cartRepository;
    private final OrderEntityRepository orderRepository;
    private final ProductEntityRepository productRepository;
    @Autowired
    private UserEntityRepository userRepository; // Inject this!


    public OrderController(CartEntityRepository cartRepository,
                           OrderEntityRepository orderRepository,
                           ProductEntityRepository productRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

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


    @Transactional
    @Operation(summary = "Check out the cart", description = "Creates an order from the current user's cart and redirects to the order confirmation")
    @ApiResponse(responseCode = "302", description = "Checkout processed; redirecting to the order confirmation or cart")
    @PostMapping("/cart/checkout")
    public String checkout(Principal principal) {
        CartEntity cart = getCartForCurrentUser(principal);
        if (cart == null || cart.getProducts().isEmpty()) {
            return "redirect:/cart?checkoutError=emptyCart";
        }

        List<ProductEntity> products = new ArrayList<>(cart.getProducts());
        boolean outOfStock = products.stream()
                .filter(PublicationEntity.class::isInstance)
                .map(PublicationEntity.class::cast)
                .anyMatch(publication -> publication.getCopies() <= 0);

        if (outOfStock) {
            return "redirect:/cart?checkoutError=outOfStock";
        }

        OrderEntity order = new OrderEntity();
        order.setOrderDate(LocalDateTime.now());

        double totalAmount = 0.0;
        for (ProductEntity product : products) {
            totalAmount += product.getPrice();

            if (product instanceof PublicationEntity publication) {
                publication.setCopies(publication.getCopies() - 1);
            }

            order.addProduct(product);
        }
        order.setTotalAmount(totalAmount);

        cart.getProducts().clear();

        productRepository.saveAll(products);
        OrderEntity savedOrder = orderRepository.save(order);
        cartRepository.save(cart);

        return "redirect:/orders/confirmation/" + savedOrder.getId();
    }

    @Operation(summary = "View an order confirmation", description = "Returns the HTML view containing the selected order's details")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the order confirmation")
    @GetMapping("/orders/confirmation/{id}")
    public String viewOrderConfirmation(@PathVariable Long id, Model model) {
        OrderEntity order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/books";
        }

        model.addAttribute("order", order);
        return "order/orderDetails";
    }
}
