package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.model.Decision;
import net.ionoff.forex.ea.model.Order;
import net.ionoff.forex.ea.repository.CandleV300Repository;
import net.ionoff.forex.ea.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(Order order) {
        Order o = orderRepository.findByTicket(order.getTicket());
        if (o == null) {
            return orderRepository.save(order);
        } else {
            order.setId(o.getId());
            return orderRepository.save(order);
        }
    }
}
