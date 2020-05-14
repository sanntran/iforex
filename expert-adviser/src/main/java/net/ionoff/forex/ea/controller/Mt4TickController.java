package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.model.slope.V300Slope;
import net.ionoff.forex.ea.service.Mt4TickService;
import net.ionoff.forex.ea.service.V300CandleService;
import net.ionoff.forex.ea.service.V300SlopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@RestController
@RequestMapping("/ticks")
public class Mt4TickController {

    @Autowired
    Mt4TickService mt4TickService;

    @Autowired
    V300CandleService v300CandleService;

    @Autowired
    V300SlopeService v300SlopeService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String newTick(@RequestParam("symbol") String symbol,
                         @RequestParam(name = "ticktime", required = false) String tickTime,
                         @RequestParam(name = "tickbid", required = false) double tickBid,
                         @RequestParam(name = "tickask", required = false) double tickAsk,
                         @RequestParam(name = "ticklast", required = false) double tickLast,
                         @RequestParam(name = "error", required = false) String error,

                         HttpServletRequest request) throws ParseException {

        mt4TickService.handleTick(symbol, tickTime, tickBid, tickAsk, tickLast, error);

        return "hihi";
    }

    @RequestMapping(value = "/import", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String importTick(HttpServletRequest request) {

        v300CandleService.importV300Candles();

        return "hihi";
    }

    @RequestMapping(value = "/slopes", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String slopes(HttpServletRequest request) {

        v300SlopeService.buildData();

        return "hihi";
    }
}
