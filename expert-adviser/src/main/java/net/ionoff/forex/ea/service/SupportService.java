package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.repository.AverageRepository;
import net.ionoff.forex.ea.repository.CandleRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SupportService {

    private CandleRepository candleRepository;
    private AverageRepository averageRepository;

}
