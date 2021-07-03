package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.exception.MethodNotSupportedException;
import net.ionoff.forex.ea.model.Action;
import net.ionoff.forex.ea.model.Order;
import net.ionoff.forex.ea.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/actions")
public class ActionController {

    @Autowired
    private ActionService actionService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Action requestActions(@RequestParam("method") String method,
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
        if (HttpMethod.GET.name().equals(method)) {
            if (ticket == null || ticket == 0) {
                return actionService.getAction();
            } else {
                Order order = Order.builder()
                        .ticket(ticket)
                        .type(type)
                        .lots(lots)
                        .openPrice(openPrice)
                        .openTime(openTime == null ? null : OffsetDateTime.parse(openTime).toInstant())
                        .profit(profit)
                        .stopLoss(stopLoss)
                        .takeProfit(takeProfit)
                        .swap(swap)
                        .comment(comment)
                        .build();
                return actionService.getAction(order);
            }
        }
        throw new MethodNotSupportedException(method);
    }
}
