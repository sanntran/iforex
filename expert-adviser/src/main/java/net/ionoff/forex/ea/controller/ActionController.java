package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.exception.MethodNotSupportedException;
import net.ionoff.forex.ea.model.Action;
import net.ionoff.forex.ea.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actions")
public class ActionController {

    @Autowired
    private ActionService actionService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Action requestActions(@RequestParam("method") String method,
                                  @RequestParam("symbol") String symbol) {
        if (HttpMethod.GET.name().equals(method)) {
            return actionService.getAction();
        }
        throw new MethodNotSupportedException(method);
    }
}
