package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.entity.Order;
import net.ionoff.forex.ea.mapper.OrderMapper;
import net.ionoff.forex.ea.model.OrderDto;
import net.ionoff.forex.ea.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(OrderDto orderDto) {
        Order order = OrderMapper.fromDto(orderDto);
        Order entity = orderRepository.findByTicket(order.getTicket());
        if (entity == null) {
            return orderRepository.save(order);
        } else {
            order.setId(entity.getId());
            return orderRepository.save(order);
        }
    }
}
