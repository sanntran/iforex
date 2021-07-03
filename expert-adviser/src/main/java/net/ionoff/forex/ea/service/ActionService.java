package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Action;
import net.ionoff.forex.ea.model.Order;
import net.ionoff.forex.ea.repository.OrderRepository;
import net.ionoff.forex.ea.stratergy.ExtremaPointStrategy;
import net.ionoff.forex.ea.stratergy.MovingAverageStrategy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ActionService {

    private OrderRepository orderRepository;
    private MovingAverageStrategy movingAverageStrategy;
    private ExtremaPointStrategy extremaPointStrategy;

    public Action getAction() {
        Action action = extremaPointStrategy.getAction();
        return action.isNoOrder() ? action : saveOrder(action);
    }

    private Action saveOrder(Action action) {
        orderRepository.save(action.getOrder());
        return action;
    }

    public Action getAction(Order order) {
        Order ticket = saveOrder(order);
        if (ticket.isClosed()) {
            return Action.closeOrder(order);
        }
        return extremaPointStrategy.getAction(ticket);
    }

    private Order saveOrder(Order order) {
        Optional<Order> ticket = orderRepository.findByTicket(order.getTicket());
        if (!ticket.isPresent()) {
            return orderRepository.save(Order.builder()
                    .ticket(order.getTicket())
                    .openTime(order.getOpenTime())
                    .openPrice(order.getOpenPrice())
                    .lots(order.getLots())
                    .type(order.getType())
                    .swap(order.getSwap())
                    .takeProfit(order.getTakeProfit())
                    .stopLoss(order.getStopLoss())
                    .comment(order.getComment())
                    .build());
        } else {
            return updateOrder(order, ticket.get());
        }
    }

    private Order updateOrder(Order order, Order ticket) {
        ticket.setProfit(order.getProfit());
        return orderRepository.save(ticket);
    }
}
