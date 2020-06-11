package net.ionoff.forex.ea.mapper;

import net.ionoff.forex.ea.entity.Order;
import net.ionoff.forex.ea.model.OrderDto;

import java.time.Instant;
import java.time.OffsetDateTime;

public class OrderMapper {

    public static Order fromDto(OrderDto dto) {
        return Order.builder()
                .ticket(dto.getTicket())
                .type(dto.getType())
                .lots(dto.getLots())
                .openPrice(dto.getOpenPrice())
                .openTime(toInstant(dto.getOpenTime()))
                .profit(dto.getProfit())
                .stopLoss(dto.getStopLoss())
                .takeProfit(dto.getTakeProfit())
                .swap(dto.getSwap())
                .build();
    }

    private static Instant toInstant(String time) {
        return OffsetDateTime.parse(time).toInstant();
    }
}
