package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.exception.SymbolNotSupportedException;
import net.ionoff.forex.ea.model.Message;
import net.ionoff.forex.ea.model.Order;
import net.ionoff.forex.ea.model.Symbol;
import net.ionoff.forex.ea.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final String MT4_DATE_TIME_FORMAT = "";

    @Autowired
    private OrderService orderService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Message orders(@RequestParam("method") String method,
                          @RequestParam("symbol") String symbol,
                          @RequestParam(name = "ticket", required = false) Long ticket,
                          @RequestParam(name = "type", required = false) Integer type,
                          @RequestParam(name = "lots", required = false) Double lots,
                          @RequestParam(name = "openPrice", required = false) Double openPrice,
                          @RequestParam(name = "openTime", required = false) String openTime,
                          @RequestParam(name = "profit", required = false) Double profit,
                          @RequestParam(name = "stopLoss", required = false) Double stopLoss,
                          @RequestParam(name = "takeProfit", required = false) Double takeProfit,
                          @RequestParam(name = "swap", required = false) Double swap) {
        if (!Symbol.EURUSD.equals(symbol)) {
            throw new SymbolNotSupportedException(symbol);
        }
        Order order = Order.builder()
                .ticket(ticket)
                .type(type)
                .lots(lots)
                .openPrice(openPrice)
                .openTime(toInstant(openTime))
                .profit(profit)
                .stopLoss(stopLoss)
                .takeProfit(takeProfit)
                .swap(swap)
                // .commission(comission) // only for share
                .build();

        orderService.saveOrder(order);
        return Message.builder().code(HttpStatus.OK.value()).build();
    }

    private Instant toInstant(String date) {
        return null;
    }
}
