package csd230.lab1.repositories;

import csd230.lab1.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findByTitle(String title);
    List<BookEntity> findByTitleLike(String title);
    List<BookEntity> findByAuthor(String author);
    List<BookEntity> findByIsbn(String isbn);

    @Query("SELECT b FROM BookEntity b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<BookEntity> searchByAuthor(@Param("author") String author);
}
