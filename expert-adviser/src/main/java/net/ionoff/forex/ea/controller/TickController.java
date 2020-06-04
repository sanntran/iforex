package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.model.Decision;
import net.ionoff.forex.ea.model.Order;
import net.ionoff.forex.ea.service.TickService;
import net.ionoff.forex.ea.service.CandleV300Service;
import net.ionoff.forex.ea.service.PatternV300Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@RestController
@RequestMapping("/ticks")
public class TickController {

    @Autowired
    TickService tickService;

    @Autowired
    CandleV300Service candleV300Service;

    @Autowired
    PatternV300Service patternV300Service;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Decision newTick(@RequestParam("symbol") String symbol,
                            @RequestParam(name = "ticktime", required = false) String tickTime,
                            @RequestParam(name = "tickbid", required = false) double tickBid,
                            @RequestParam(name = "tickask", required = false) double tickAsk,
                            @RequestParam(name = "ticklast", required = false) double tickLast,
                            @RequestParam(name = "error", required = false) String error) {

        tickService.handleTick(symbol, tickTime, tickBid, tickAsk, tickLast, error);
        System.out.println(symbol + tickTime);
        return Decision.builder()
                .action(Decision.ACTION.NO_ORDER.name())
                .order(Order.builder().type(Order.TYPE.BUY.getValue()).build()).build();
    }

    @RequestMapping(value = "/import", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String importTick(HttpServletRequest request) {

        candleV300Service.importV300Candles();

        return "hihi";
    }

    @RequestMapping(value = "/patterns", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String distance(HttpServletRequest request) {

        patternV300Service.buildData();

        return "hihi";
    }
}
