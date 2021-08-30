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

    public void createSupportAndResistance(Average newAvg) {
        if (newAvg.getClose() == null || newAvg.getSlope() == null) {
            return;
        }
        Support.Period supportPeriod = Support.Period.valueOf(newAvg.getPeriod().name());

        Support support = supportRepository.findLatest(supportPeriod).orElseGet(()-> {
            Candle lowestCandle = candleRepository.findLowest(0L, newAvg.getCandle().getId());
            return createSupport(lowestCandle, Support.Period.SHORT);
        });
        Resistance.Period resistancePeriod = Resistance.Period.valueOf(newAvg.getPeriod().name());

        Resistance resistance = resistanceRepository.findLatest(resistancePeriod).orElseGet(()-> {
            Candle highestCandle = candleRepository.findHighest(0L, newAvg.getCandle().getId());
            return createResistance(highestCandle, Resistance.Period.SHORT);
        });
        newAvg.setResistance(resistance.getCandle());
        newAvg.setSupport(support.getCandle());

        if (getPip(newAvg.getSlope()) >= supportPeriod.getSlope()) {
            // go backward to the resistance and find the lowest candle, it's support
            Support newSupport = support;
            if (newSupport.getTime().isAfter(resistance.getTime())) {
                newAvg.setSupport(newSupport.getCandle());
                return; // already have support
            } else {
                Long resistanceCandle = resistance.getCandle().getId();
                Candle lowestCandle = candleRepository.findLowest(resistanceCandle, newAvg.getCandle().getId());
                newSupport = createSupport(lowestCandle, Support.Period.SHORT);
                newAvg.setSupport(newSupport.getCandle());
            }
        } else if (getPip(newAvg.getSlope()) <= resistancePeriod.getSlope()) {
            Resistance newResistance = resistance;
            if (resistance.getTime().isAfter(support.getTime())) {
                newAvg.setResistance(newResistance.getCandle());
                return; // already have resistance
            } else {
                Long supportCandle = support.getCandle().getId();
                Candle highestCandle = candleRepository.findHighest(supportCandle, newAvg.getCandle().getId());
                newResistance = createResistance(highestCandle, Resistance.Period.SHORT);
                newAvg.setResistance(newResistance.getCandle());
            }
        }
    }

    private Support createSupport(Candle candle, Support.Period period) {
        return supportRepository.save(
                Support.builder()
                        .candle(candle)
                        .time(candle.getTime())
                        .close(candle.getClose())
                        .period(period)
                        .build()
        );
    }

    private Resistance createResistance(Candle candle, Resistance.Period period) {
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
