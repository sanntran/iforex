package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.service.Mt4TickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@RestController
@RequestMapping("/ticks")
public class Mt4TickController {

    @Autowired
    Mt4TickService mt4TickService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String onTick(@RequestParam("symbol") String symbol,
                         @RequestParam(name = "ticktime", required = false) String tickTime,
                         @RequestParam(name = "tickbid", required = false) double tickBid,
                         @RequestParam(name = "tickask", required = false) double tickAsk,
                         @RequestParam(name = "ticklast", required = false) double tickLast,
                         @RequestParam(name = "error", required = false) String error,

                         HttpServletRequest request) throws ParseException {

        mt4TickService.handleTick(symbol, tickTime, tickBid, tickAsk, tickLast, error);

        return "hihi";
    }

}
