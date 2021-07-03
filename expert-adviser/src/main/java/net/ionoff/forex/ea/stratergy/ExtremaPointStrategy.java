package net.ionoff.forex.ea.stratergy;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Action;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Order;
import net.ionoff.forex.ea.repository.AverageRepository;
import net.ionoff.forex.ea.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;
import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Component
@AllArgsConstructor
public class ExtremaPointStrategy {

    public static final double SPREAD = 0.00007;
    private AverageRepository averageRepository;
    private OrderRepository orderRepository;

    public Action getAction() {
        Average avg = averageRepository.findLatest().orElse(null);
        if (avg == null) {
            return Action.noOrder();
        }

        if (avg.isShortAvgGoingDownAndBreakSupport()) {
            System.out.println("OPEN ORDER SELL");
            return Action.openOrder(
                    Order.builder()
                            .type(Order.TYPE.SELL.getValue())
                            .lots(1d)
                            .openPrice(avg.getClose())
                            .openTime(avg.getCloseTime())
                            .takeProfit(avg.getClose() - 0.01)
                            .stopLoss(avg.getClose() + 0.01)
                            .build());
        } else if (avg.isShortAvgGoingUpAndBreakResistance()) {
            System.out.println("OPEN ORDER BUY");
            return Action.openOrder(Order.builder()
                    .type(Order.TYPE.BUY.getValue())
                    .lots(1d)
                    .openPrice(avg.getClose())
                    .openTime(avg.getCloseTime())
                    .takeProfit(avg.getClose() + 0.01)
                    .stopLoss(avg.getClose() - 0.01)
                    .build());
        }
        return Action.noOrder();
    }

    public Action getAction(Order order) {
        Average avg = averageRepository.findLatest().orElse(null);
        if (avg == null) {
            return Action.noOrder();
        }
        if (order.isBuy() && avg.isShortAvgGoingDownAndBreakSupport()) {
            if (!order.isWaitingForClose()) {
                order.setWaitingForClose(true);
                orderRepository.save(order);
            }
        } else if (order.isSell() && avg.isShortAvgGoingUpAndBreakResistance()) {
            if (!order.isWaitingForClose()) {
                order.setWaitingForClose(true);
                orderRepository.save(order);
            }
        } else if (order.isSell() && order.isWaitingForClose() && avg.isShortAvgGoingDown()) {
            return closeOrderSell(order, avg);
        } else if (order.isBuy() && order.isWaitingForClose() && avg.isShortAvgGoingUp()) {
            return closeOrderBuy(order, avg);
        }
        return Action.noOrder();
    }

    private Action closeOrderBuy(Order order, Average avg) {
        Action action = Action.closeOrder(order);
        action.getOrder().setCloseTime(avg.getCloseTime());
        action.getOrder().setClosePrice(avg.getClose());
        return action;
    }

    private Action closeOrderSell(Order order, Average avg) {
        Action action = Action.closeOrder(order);
        action.getOrder().setCloseTime(avg.getCloseTime());
        action.getOrder().setClosePrice(avg.getClose() + SPREAD);
        return action;
    }

}
