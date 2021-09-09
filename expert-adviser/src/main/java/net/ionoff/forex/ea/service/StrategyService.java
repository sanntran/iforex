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
import java.util.List;
import java.util.Optional;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Component
@AllArgsConstructor
public class StrategyService {

    private OrderRepository orderRepository;
    private ResultRepository resultRepository;
    private CandleRepository candleRepository;
    private MovingAverageStrategy movingAverageStrategy;

    public Message testStrategy(Instant fromDate, Instant toDate) {
        List<Candle> candles = candleRepository.findByDateRange(Candle.Period.SHORT.name(), fromDate, toDate);
        for (Candle candle : candles) {
            Order order = orderRepository.findOpen().orElse(null);
            Action action = order != null && order.isOpen()
                    ? movingAverageStrategy.getAction(order, candle)
                    : movingAverageStrategy.getAction(candle);
            if (Action.Code.CLOSE.getValue() == action.getCode()) {
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
            } else if (Action.Code.OPEN.getValue() == action.getCode()) {
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
        return Message.ok();
    }
}
