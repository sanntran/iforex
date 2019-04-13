package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.model.Decision;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class Mt4TickService {

    public Decision handleTick(String symbol,
                               String tickTime,
                               double tickBid,
                               double tickAsk,
                               double tickLast,
                               String error) {

        return null;
        
    }
}
