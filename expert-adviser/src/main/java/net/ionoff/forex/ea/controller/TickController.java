package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.entity.Decision;
import net.ionoff.forex.ea.entity.Order;
import net.ionoff.forex.ea.model.TickDto;
import net.ionoff.forex.ea.service.CandleV300Service;
import net.ionoff.forex.ea.service.PatternV300Service;
import net.ionoff.forex.ea.service.TickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;

import static net.ionoff.forex.ea.constant.Symbol.assertValid;

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
                            @RequestParam(name = "time") String time,
                            @RequestParam(name = "bid") Double bid,
                            @RequestParam(name = "ask") Double ask
                                ) {
        assertValid(symbol);

        tickService.handleTick(TickDto.builder().time(time).ask(ask).bid(bid).build());
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
