package ru.kpfu.itis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.models.Cart;

import java.util.List;

@Repository
@Transactional
public interface CartRepo extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserId(int id);

    @Modifying
    @Query(value = "delete from book_cart where book_id=?1 and user_id=?2", nativeQuery = true)
    void deleteCartById(int id, int user_id);
}
