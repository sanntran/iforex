package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.event.CandleClosedEvent;
import net.ionoff.forex.ea.event.CandleEventNotifier;
import net.ionoff.forex.ea.model.*;
import net.ionoff.forex.ea.repository.CandleRepository;
import net.ionoff.forex.ea.repository.OrderRepository;
import net.ionoff.forex.ea.repository.ResultRepository;
import net.ionoff.forex.ea.repository.TickRepository;
import net.ionoff.forex.ea.stratergy.MovingAverageStrategy;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Component
@AllArgsConstructor
public class StrategyService {

    private OrderRepository orderRepository;
    private ResultRepository resultRepository;
    private CandleRepository candleRepository;
    private CandleEventNotifier candleEventNotifier;
    private MovingAverageStrategy movingAverageStrategy;

    public Message testStrategy() {
        for (long id = 1; true; id++) {
            Candle candle = candleRepository.findById(id).orElse(null);
            if (candle == null) {
                break;
            }
            candleEventNotifier.fireCandleEvent(new CandleClosedEvent(candle));
            Order order = orderRepository.findLatest().orElse(null);
            Action action = order != null && order.isOpen()
                    ? movingAverageStrategy.getAction(order)
                    : movingAverageStrategy.getAction();
            if (Action.Code.CLOSE_ORDER.getValue() == action.getCode()) {
                Order orderToClose = action.getOrder();
                orderToClose.setProfit(orderToClose.isBuy()
                        ? getPip(orderToClose.getClosePrice() - orderToClose.getOpenPrice())
                        : getPip(orderToClose.getOpenPrice() - orderToClose.getClosePrice())
                );
                orderRepository.save(orderToClose);
                resultRepository.save(
                        Result.builder()
                                .time(orderToClose.getCloseTime())
                                .type(Result.CLOSE)
                                .size(orderToClose.getLots())
                                .price(orderToClose.getClosePrice())
                                .profit(orderToClose.getProfit())
                                .orderId(orderToClose.getId())
                                .build());
            } else if (Action.Code.OPEN_ORDER.getValue() == action.getCode()) {
                Order orderToOpen = action.getOrder();
                orderRepository.save(orderToOpen);
                resultRepository.save(
                        Result.builder()
                                .time(orderToOpen.getOpenTime())
                                .type(Result.OPEN)
                                .size(orderToOpen.getLots())
                                .price(orderToOpen.getOpenPrice())
                                .orderId(orderToOpen.getId())
                                .build());
            }
        }
        return Message.candleUpdated();
    }
}
