package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Action;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.Order;
import net.ionoff.forex.ea.repository.CandleRepository;
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
    private CandleRepository candleRepository;

    public Action getAction() {
        Action action = movingAverageStrategy.getAction(candleRepository.findLatest().orElse(null));
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
        Action action = movingAverageStrategy.getAction(ticket, candleRepository.findLatest().orElse(null));
        return action.isNoOrder() ? action : saveOrder(action);
    }

    private Order saveOrder(Order order) {
        Optional<Order> ticket = orderRepository.findLatest();
        if (!ticket.isPresent() || ticket.get().isClosed()) {
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
        ticket.setTicket(order.getTicket());
        ticket.setProfit(order.getProfit());
        return orderRepository.save(ticket);
    }
}
