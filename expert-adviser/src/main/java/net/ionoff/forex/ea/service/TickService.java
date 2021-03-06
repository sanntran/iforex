package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.model.Decision;
import net.ionoff.forex.ea.repository.CandleV300Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TickService {

    @Autowired
    CandleV300Repository v300CandleRepository;

    public Decision handleTick(String symbol,
                               String tickTime,
                               double tickBid,
                               double tickAsk,
                               double tickLast,
                               String error) {

        return null;
        
    }


}
