package csd230.lab1.repositories;

import csd230.lab1.entities.MagazineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MagazineRepository extends JpaRepository<MagazineEntity, Long> {
    @Query("select magazine from MagazineEntity magazine where type(magazine) = MagazineEntity")
    List<MagazineEntity> findAllMagazines();

    List<MagazineEntity> findByTitle(String title);
    List<MagazineEntity> findByTitleLike(String title);
}
