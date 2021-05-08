package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.model.Action;
import net.ionoff.forex.ea.model.Message;
import net.ionoff.forex.ea.model.Tick;
import net.ionoff.forex.ea.exception.MethodNotSupportedException;
import net.ionoff.forex.ea.service.TickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static net.ionoff.forex.ea.constant.Symbol.assertValid;

@RestController
@RequestMapping("/ticks")
public class TickController {

    @Autowired
    private TickService tickService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Message handleTick(@RequestParam("symbol") String symbol,
                              @RequestParam("method") String method,
                              @RequestParam(name = "time") String time,
                              @RequestParam(name = "bid") Double bid,
                              @RequestParam(name = "ask") Double ask) {
        assertValid(symbol);
        if (HttpMethod.POST.name().equals(method)) {
            System.out.println(time);
            Tick tick = Tick.builder()
                            .time(toInstant(time))
                            .ask(bid)
                            .bid(ask)
                            .build();
            tickService.handleTick(tick);
            return new Message(HttpStatus.OK.value());
        }
        throw new MethodNotSupportedException(method);
    }

    private static Instant toInstant(String time) {
        return OffsetDateTime.parse(time).toInstant();
    }
}
