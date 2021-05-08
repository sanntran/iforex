package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.model.Tick;
import net.ionoff.forex.ea.repository.TickRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TickService {

    @Autowired
    private TickRepository tickRepository;

    public Tick handleTick(Tick tick) {
        System.out.println("new tick: " + tick.toString());
        return tick;
        //return tickRepository.save(tick);
    }
}
