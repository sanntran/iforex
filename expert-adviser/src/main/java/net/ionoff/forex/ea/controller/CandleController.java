package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.exception.MethodNotSupportedException;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.Message;
import net.ionoff.forex.ea.service.CandleService;
import net.ionoff.forex.ea.service.TickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/candles")
public class CandleController {

    @Autowired
    private TickService tickService;

    @Autowired
    private CandleService candleService;
    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Message handleCandle(@RequestParam("symbol") String symbol,
                                @RequestParam("method") String method,
                                @RequestParam(name = "time") String time,
                                @RequestParam(name = "open") Double open,
                                @RequestParam(name = "close") Double close,
                                @RequestParam(name = "high") Double high,
                                @RequestParam(name = "low") Double low,
                                @RequestParam(name = "volume") Integer volume
    ) {
        if (HttpMethod.POST.name().equals(method)) {
            Candle candle = Candle.builder()
                    .time(toInstant(time))
                    .open(open)
                    .close(close)
                    .low(low)
                    .high(high)
                    .volume(volume)
                    .build();
            return candleService.createCandle(candle);
        }
        throw new MethodNotSupportedException(method);
    }

    private static Instant toInstant(String time) {
        return OffsetDateTime.parse(time).toInstant();
    }
}
