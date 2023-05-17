package ru.kpfu.itis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.models.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {

    Category findByName(String name);

}
