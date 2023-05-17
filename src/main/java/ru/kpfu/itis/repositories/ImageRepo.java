package ru.kpfu.itis.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.models.Image;

@Repository
public interface ImageRepo extends JpaRepository<Image, Integer> {
}
