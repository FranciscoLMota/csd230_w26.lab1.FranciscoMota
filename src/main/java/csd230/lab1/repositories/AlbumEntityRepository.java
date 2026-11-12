package csd230.lab1.repositories;

import csd230.lab1.entities.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumEntityRepository extends JpaRepository<AlbumEntity, Long> {
    List<AlbumEntity> findByTitle(String title);
    List<AlbumEntity> findByTitleLike(String title);
    List<AlbumEntity> findByArtist(String artist);
    List<AlbumEntity> findByGenre(String genre);
    List<AlbumEntity> findByReleaseYear(Integer releaseYear);

    @Query("SELECT a FROM AlbumEntity a WHERE LOWER(a.artist) LIKE LOWER(CONCAT('%', :artist, '%'))")
    List<AlbumEntity> searchByArtist(@Param("artist") String artist);
}
