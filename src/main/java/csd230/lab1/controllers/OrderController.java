package csd230.lab1.controllers;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.OrderEntity;
import csd230.lab1.entities.ProductEntity;
import csd230.lab1.entities.PublicationEntity;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.OrderEntityRepository;
import csd230.lab1.repositories.ProductEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    private static final long DEFAULT_CART_ID = 1L;

    private final CartEntityRepository cartRepository;
    private final OrderEntityRepository orderRepository;
    private final ProductEntityRepository productRepository;

    public OrderController(CartEntityRepository cartRepository,
                           OrderEntityRepository orderRepository,
                           ProductEntityRepository productRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @PostMapping("/cart/checkout")
    public String checkout() {
        CartEntity cart = cartRepository.findById(DEFAULT_CART_ID).orElse(null);
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
