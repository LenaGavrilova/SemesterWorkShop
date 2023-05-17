package ru.kpfu.itis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.models.Order;
import ru.kpfu.itis.models.Status;
import ru.kpfu.itis.models.User;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {

    List<Order> findByUser(User user);

    @Modifying
    @Query(value = "update orders set status=?1 where id=?2", nativeQuery = true)
    void updateOrderStatus(int status, int id);
}
