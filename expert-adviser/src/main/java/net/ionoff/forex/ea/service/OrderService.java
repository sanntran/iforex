package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.model.Order;
import net.ionoff.forex.ea.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(Order order) {
        Optional<Order> entity = orderRepository.findByTicket(order.getTicket());
        if (entity.isPresent()) {
            order.setId(entity.get().getId());
            return orderRepository.save(order);
        } else {
            return orderRepository.save(order);
        }
    }
}
