package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.model.ActionDto;
import net.ionoff.forex.ea.model.TickDto;
import net.ionoff.forex.ea.repository.CandleV300Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class TickService {

    @Autowired
    private CandleV300Repository v300CandleRepository;

    public Optional<ActionDto> handleTick(TickDto tickDto) {
        return Optional.empty();
    }
}
