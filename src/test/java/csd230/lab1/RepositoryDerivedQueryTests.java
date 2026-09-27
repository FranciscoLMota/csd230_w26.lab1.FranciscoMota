package csd230.lab1;

import csd230.lab1.entities.BookEntity;
import csd230.lab1.entities.DiscMagEntity;
import csd230.lab1.entities.TicketEntity;
import csd230.lab1.repositories.BookRepository;
import csd230.lab1.repositories.DiscMagRepository;
import csd230.lab1.repositories.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class RepositoryDerivedQueryTests {

    @Autowired private BookRepository bookRepository;
    @Autowired private DiscMagRepository discMagRepository;
    @Autowired private TicketRepository ticketRepository;

    @Test
    void findsBooksByExactAndPartialTitle() {
        BookEntity book = bookRepository.saveAndFlush(
                new BookEntity("Dungeon Master's Guide", 59.99, 3,
                        "Wizards of the Coast", "9780786968529"));

        assertThat(bookRepository.findByTitle("Dungeon Master's Guide")).containsExactly(book);
        assertThat(bookRepository.findByTitleLike("%Dungeon Master%")).containsExactly(book);
        assertThat(bookRepository.findByTitle("Missing Spellbook")).isEmpty();
    }

    @Test
    void findsBooksByAuthorIsbnAndCustomJpqlQuery() {
        BookEntity book = bookRepository.saveAndFlush(
                new BookEntity("Player's Handbook", 59.99, 2,
                        "Wizards of the Coast", "9780786969517"));

        assertThat(bookRepository.findByAuthor("Wizards of the Coast")).containsExactly(book);
        assertThat(bookRepository.findByIsbn("9780786969517")).containsExactly(book);
        assertThat(bookRepository.searchByAuthor("wizards of the coast")).containsExactly(book);
    }

    @Test
    void findsTicketsByExactAndPartialDescription() {
        TicketEntity ticket = ticketRepository.saveAndFlush(
                new TicketEntity("Dungeons and Dragons game night", 18.00));

        assertThat(ticketRepository.findByDescription("Dungeons and Dragons game night"))
                .containsExactly(ticket);
        assertThat(ticketRepository.findByDescriptionLike("%Dragons%"))
                .containsExactly(ticket);
    }

    @Test
    void findsDiscMagazinesByDiscAvailability() {
        DiscMagEntity discMagazine = discMagRepository.saveAndFlush(
                new DiscMagEntity("Dragon Magazine Archive", 29.99, 5, 2,
                        LocalDateTime.of(2026, 9, 1, 0, 0), true));

        assertThat(discMagRepository.findByHasDisc(true)).contains(discMagazine);
        assertThat(discMagRepository.findByTitle("Dragon Magazine Archive"))
                .containsExactly(discMagazine);
    }
}
