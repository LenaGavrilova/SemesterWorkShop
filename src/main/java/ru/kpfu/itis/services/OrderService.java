package ru.kpfu.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.models.Order;
import ru.kpfu.itis.repositories.OrderRepo;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepo orderRepo;

    @Autowired
    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public Order getOrderId(int id) {
        Optional<Order> optionalOrder = orderRepo.findById(id);
        return optionalOrder.orElse(null);
    }

    @Transactional
    public void updateOrderStatus(int id, Order order) {
        int status = order.getStatus().ordinal();
        orderRepo.updateOrderStatus(status, id);
    }
}
