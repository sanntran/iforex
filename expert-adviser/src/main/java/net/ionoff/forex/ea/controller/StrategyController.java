package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.model.Message;
import net.ionoff.forex.ea.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/strategies")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @RequestMapping(value = "/test",
            method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Message testStrategy() {
        return strategyService.testStrategy();
    }

}
