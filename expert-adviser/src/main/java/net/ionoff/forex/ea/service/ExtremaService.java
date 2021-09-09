package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.Resistance;
import net.ionoff.forex.ea.model.Support;
import net.ionoff.forex.ea.repository.AverageRepository;
import net.ionoff.forex.ea.repository.CandleRepository;
import net.ionoff.forex.ea.repository.ResistanceRepository;
import net.ionoff.forex.ea.repository.SupportRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Component
@AllArgsConstructor
public class ExtremaService {

    private CandleRepository candleRepository;
    private SupportRepository supportRepository;
    private ResistanceRepository resistanceRepository;

    public Support createSupport(Candle candle, Support.Period period) {
        return supportRepository.save(
                Support.builder()
                        .candle(candle)
                        .time(candle.getTime())
                        .close(candle.getClose())
                        .period(period)
                        .build()
        );
    }

    public Support getOrCreateSupport(Candle candle, Support.Period period) {
        return supportRepository.findLatest(period.name()).orElseGet(()-> {
            Candle lowestCandle = candleRepository.findLowest(period.name(), 0L, candle.getId());
            return createSupport(lowestCandle, period);
        });
    }

    public Resistance getOrCreateResistance(Candle candle, Resistance.Period period) {
        return resistanceRepository.findLatest(period.name()).orElseGet(()-> {
            Candle highestCandle = candleRepository.findHighest(period.name(), 0L, candle.getId());
            return createResistance(highestCandle, period);
        });
    }

    public Resistance createResistance(Candle candle, Resistance.Period period) {
        return resistanceRepository.save(
                Resistance.builder()
                        .candle(candle)
                        .time(candle.getTime())
                        .close(candle.getClose())
                        .period(period)
                        .build()
        );
    }
}
