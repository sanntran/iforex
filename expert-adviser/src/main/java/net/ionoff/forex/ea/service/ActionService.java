package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.constant.Operation;
import net.ionoff.forex.ea.model.ActionDto;
import net.ionoff.forex.ea.repository.OrderRepository;
import net.ionoff.forex.ea.repository.PatternV300Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;

@Component
@Transactional
public class ActionService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PatternV300Repository patternV300Repository;

    public Optional<ActionDto> getAction() {
        if (Calendar.getInstance().get(Calendar.MINUTE) == 7) {
            return Optional.of(ActionDto.builder().type(Operation.OP_BUY.getValue()).build());
        }
        return Optional.empty();
    }
}
