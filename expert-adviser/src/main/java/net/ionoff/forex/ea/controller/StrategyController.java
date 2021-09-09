package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.model.Message;
import net.ionoff.forex.ea.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/strategies")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @RequestMapping(value = "/test",
            method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Message testStrategy(@RequestParam(name = "fromDate") String fromDate,
                                @RequestParam(name = "toDate") String toDate) {
        return strategyService.testStrategy(
                OffsetDateTime.parse(fromDate).toInstant(),
                OffsetDateTime.parse(toDate).toInstant());
    }

}
