package csd230.lab1;

import csd230.lab1.entities.BookEntity;
import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.TicketEntity;
import csd230.lab1.repositories.BookRepository;
import csd230.lab1.repositories.CartEntityRepository;
import csd230.lab1.repositories.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class RepositoryCrudTests {

    @Autowired private BookRepository bookRepository;
    @Autowired private CartEntityRepository cartEntityRepository;
    @Autowired private TicketRepository ticketRepository;

    @Test
    void ticketRepositorySupportsCrud() {
        TicketEntity ticket = ticketRepository.saveAndFlush(
                new TicketEntity("Waterdeep campaign session", 15.00));
        Long ticketId = ticket.getId();

        assertThat(ticketId).isNotNull();
        assertThat(ticketRepository.findById(ticketId)).contains(ticket);

        ticket.setDescription("Waterdeep campaign finale");
        ticketRepository.saveAndFlush(ticket);
        assertThat(ticketRepository.findById(ticketId)).get()
                .extracting(TicketEntity::getDescription).isEqualTo("Waterdeep campaign finale");

        ticketRepository.delete(ticket);
        ticketRepository.flush();
        assertThat(ticketRepository.findById(ticketId)).isEmpty();
    }

    @Test
    void bookRepositorySupportsCrud() {
        BookEntity book = bookRepository.saveAndFlush(
                new BookEntity("Mordenkainen Presents: Monsters of the Multiverse",
                        59.99, 3, "Wizards of the Coast", "9780786967872"));
        Long bookId = book.getId();

        assertThat(bookRepository.findById(bookId)).contains(book);

        book.setCopies(7);
        book.setPrice(34.99);
        bookRepository.saveAndFlush(book);
        assertThat(bookRepository.findById(bookId)).get().satisfies(savedBook -> {
            assertThat(savedBook.getCopies()).isEqualTo(7);
            assertThat(savedBook.getPrice()).isEqualTo(34.99);
        });

        bookRepository.deleteById(bookId);
        bookRepository.flush();
        assertThat(bookRepository.existsById(bookId)).isFalse();
    }

    @Test
    void cartRepositorySupportsCreateReadAndDelete() {
        CartEntity cart = new CartEntity();
        cart = cartEntityRepository.saveAndFlush(cart);
        Long cartId = cart.getId();

        assertThat(cartId).isNotNull();
        assertThat(cartEntityRepository.findById(cartId)).contains(cart);

        cartEntityRepository.delete(cart);
        cartEntityRepository.flush();
        assertThat(cartEntityRepository.existsById(cartId)).isFalse();
    }
}
