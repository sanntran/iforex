package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.service.M1CandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@RestController
@RequestMapping("/candles")
public class M1CandleController {

    @Autowired
    M1CandleService m1CandleService;

    @RequestMapping(value = "/m1", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String saveM1Candle(@RequestParam("symbol") String symbol,
                         @RequestParam("m1BarTime") String m1BarTime, @RequestParam("m1BarOpen") double m1BarOpen,
                         @RequestParam("m1BarHigh") double m1BarHigh, @RequestParam("m1BarLow") double m1BarLow,
                         @RequestParam("m1BarClose") double m1BarClose, @RequestParam("m1BarVolume") long m1BarVolume,
                         HttpServletRequest request) throws ParseException {

        m1CandleService.saveM1Candle(symbol, m1BarTime, m1BarOpen, m1BarHigh, m1BarLow, m1BarClose, m1BarVolume);
        return "Hihi";
    }

}
