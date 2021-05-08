package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.model.Action;
import net.ionoff.forex.ea.model.Order;
import net.ionoff.forex.ea.model.Tick;
import net.ionoff.forex.ea.repository.OrderRepository;
import net.ionoff.forex.ea.repository.TickRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ActionService {

    @Autowired
    private TickRepository tickRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Action getAction() {
        return Action.noOrder();
    }
}
