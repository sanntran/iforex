package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.exception.MethodNotSupportedException;
import net.ionoff.forex.ea.model.OrderDto;
import net.ionoff.forex.ea.model.ResponseDto;
import net.ionoff.forex.ea.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static net.ionoff.forex.ea.constant.Symbol.assertValid;

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
    public ResponseDto requestOrders(@RequestParam("method") String method,
                                     @RequestParam("symbol") String symbol,
                                     @RequestParam(name = "ticket", required = false) Long ticket,
                                     @RequestParam(name = "type", required = false) Integer type,
                                     @RequestParam(name = "lots", required = false) Double lots,
                                     @RequestParam(name = "openPrice", required = false) Double openPrice,
                                     @RequestParam(name = "openTime", required = false) String openTime,
                                     @RequestParam(name = "profit", required = false) Double profit,
                                     @RequestParam(name = "stopLoss", required = false) Double stopLoss,
                                     @RequestParam(name = "takeProfit", required = false) Double takeProfit,
                                     @RequestParam(name = "swap", required = false) Double swap,
                                     @RequestParam(name = "comment", required = false) String comment) {

        assertValid(symbol);

        if (HttpMethod.PUT.name().equals(method)) {
            return saveOrder(OrderDto.builder()
                    .ticket(ticket)
                    .type(type)
                    .lots(lots)
                    .openPrice(openPrice)
                    .openTime(openTime)
                    .profit(profit)
                    .stopLoss(stopLoss)
                    .takeProfit(takeProfit)
                    .swap(swap)
                    .comment(comment)
                    .build());
        }
        throw new MethodNotSupportedException(method);
    }

    private ResponseDto saveOrder(OrderDto orderDto) {
        orderService.saveOrder(orderDto);
        return ResponseDto.builder()
                .code(HttpStatus.ACCEPTED.value())
                .build();
    }
}
