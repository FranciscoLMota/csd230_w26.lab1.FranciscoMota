package csd230.lab1;

import csd230.lab1.entities.BookEntity;
import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.ProductEntity;
import csd230.lab1.entities.TicketEntity;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.ProductEntityRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class CartProductRelationshipTests {

    @Autowired private CartEntityRepository cartRepository;
    @Autowired private ProductEntityRepository productRepository;
    @Autowired private EntityManager entityManager;

    @Test
    void savingCartCascadesToItsProducts() {
        CartEntity cart = new CartEntity();
        cart.addProduct(new BookEntity("Tasha's Cauldron of Everything", 49.99, 2,
                "Wizards of the Coast", "9780786967025"));
        cart.addProduct(new TicketEntity("Adventurers League session", 10.00));
        Long cartId = cartRepository.saveAndFlush(cart).getId();

        entityManager.clear();

        CartEntity persistedCart = cartRepository.findById(cartId).orElseThrow();
        assertThat(persistedCart.getProducts()).hasSize(2);
        assertThat(persistedCart.getProducts())
                .extracting(product -> product.getClass().getSimpleName())
                .containsExactlyInAnyOrder("BookEntity", "TicketEntity");
    }

    @Test
    void persistedProductRetainsItsCartLink() {
        CartEntity cart = new CartEntity();
        BookEntity book = new BookEntity("Xanathar's Guide to Everything", 49.99, 4,
                "Wizards of the Coast", "9780786966110");
        cart.addProduct(book);
        Long cartId = cartRepository.saveAndFlush(cart).getId();
        Long productId = book.getId();

        entityManager.clear();

        ProductEntity persistedProduct = productRepository.findById(productId).orElseThrow();
        Set<CartEntity> linkedCarts = persistedProduct.getCarts();
        assertThat(linkedCarts).extracting(CartEntity::getId).containsExactly(cartId);
    }

    @Test
    void oneProductCanBelongToMultipleCarts() {
        BookEntity sharedBook = productRepository.saveAndFlush(
                new BookEntity("Monster Manual", 49.99, 6,
                        "Wizards of the Coast", "9780786965618"));
        CartEntity firstCart = new CartEntity();
        CartEntity secondCart = new CartEntity();
        firstCart.addProduct(sharedBook);
        secondCart.addProduct(sharedBook);
        Long firstCartId = cartRepository.saveAndFlush(firstCart).getId();
        Long secondCartId = cartRepository.saveAndFlush(secondCart).getId();
        Long productId = sharedBook.getId();

        entityManager.clear();

        ProductEntity persistedProduct = productRepository.findById(productId).orElseThrow();
        assertThat(persistedProduct.getCarts())
                .extracting(CartEntity::getId)
                .containsExactlyInAnyOrder(firstCartId, secondCartId);
    }
}
