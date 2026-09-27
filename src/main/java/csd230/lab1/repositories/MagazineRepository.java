package csd230.lab1.repositories;

import csd230.lab1.entities.MagazineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MagazineRepository extends JpaRepository<MagazineEntity, Long> {
    List<MagazineEntity> findByTitle(String title);
    List<MagazineEntity> findByTitleLike(String title);
}
