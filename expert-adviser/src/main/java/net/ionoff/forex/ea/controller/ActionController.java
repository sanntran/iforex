package net.ionoff.forex.ea.controller;

import net.ionoff.forex.ea.exception.MethodNotSupportedException;
import net.ionoff.forex.ea.model.ActionDto;
import net.ionoff.forex.ea.model.OrderDto;
import net.ionoff.forex.ea.model.ResponseDto;
import net.ionoff.forex.ea.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static net.ionoff.forex.ea.constant.Symbol.assertValid;

@RestController
@RequestMapping("/actions")
public class ActionController {

    private static final String MT4_DATE_TIME_FORMAT = "";

    @Autowired
    private ActionService actionService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseDto requestActions(@RequestParam("method") String method,
                                     @RequestParam("symbol") String symbol) {
        assertValid(symbol);
        if (HttpMethod.GET.name().equals(method)) {
            return getAction();
        }
        throw new MethodNotSupportedException(method);
    }

    private ResponseDto getAction() {
        Optional<ActionDto> action = actionService.getAction();
        if (action.isPresent()) {
            return ResponseDto.builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .build();
        }
        return ResponseDto.builder()
                .code(HttpStatus.CREATED.value())
                .body(action)
                .build();
    }
}
