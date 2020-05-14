package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.service.V300SlopeService;
import net.ionoff.forex.ea.service.M1CandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@RestController
@RequestMapping("/m1_candles")
public class M1CandleController {

    @Autowired
    M1CandleService m1CandleService;

    @Autowired
    V300SlopeService m1CandlePatternMiner;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String saveM1Candle(@RequestParam("symbol") String symbol,
                               @RequestParam("barTime") String barTime,
                               @RequestParam("barOpen") double barOpen,
                               @RequestParam("barClose") double barClose,
                               @RequestParam("barHigh") double barHigh,
                               @RequestParam("barLow") double barLow,
                               @RequestParam("barVolume") long barVolume,
                               HttpServletRequest request) throws ParseException {

        m1CandleService.saveM1Candle(symbol, barTime, barOpen, barHigh, barLow, barClose, barVolume);
        return "Hihi";
    }

    @RequestMapping(value = "/patterns", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String patternM1Candles(HttpServletRequest request) {

        m1CandlePatternMiner.buildData();
        return "Hihi";
    }
}
