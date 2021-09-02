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
    private AverageRepository averageRepository;
    private ResistanceRepository resistanceRepository;

    public void createSupportAndResistance(Average newAvg) {
        if (newAvg.getClose() == null || newAvg.getSlope() == null) {
            return;
        }
        final String candlePeriod = Candle.Period.SHORT.name();
        final Support.Period supportPeriod = Support.Period.valueOf(newAvg.getPeriod().name());
        final Resistance.Period resistancePeriod = Resistance.Period.valueOf(newAvg.getPeriod().name());
        final Support currentSupport = supportRepository.findLatest(supportPeriod.name()).orElseGet(()-> {
            Candle lowestCandle = candleRepository.findLowest(candlePeriod, 0L, newAvg.getCandle().getId());
            return createSupport(lowestCandle, supportPeriod);
        });
        final Resistance currentResistance = resistanceRepository.findLatest(resistancePeriod.name()).orElseGet(()-> {
            Candle highestCandle = candleRepository.findHighest(candlePeriod, 0L, newAvg.getCandle().getId());
            return createResistance(highestCandle, resistancePeriod);
        });
        newAvg.setResistance(currentResistance.getCandle());
        newAvg.setSupport(currentSupport.getCandle());

        if (hasNewSupport(supportPeriod, newAvg)
                && currentSupport.getTime().isBefore(currentResistance.getTime())) {
            // go backward to the resistance and find the lowest candle, it's support
            Long resistanceCandle = currentResistance.getCandle().getId();
            Candle lowestCandle = candleRepository.findLowest(candlePeriod, resistanceCandle, newAvg.getCandle().getId());
            Support newSupport = createSupport(lowestCandle, supportPeriod);
            newAvg.setSupport(newSupport.getCandle());
        } else if (hasNewResistance(resistancePeriod, newAvg)
            && currentResistance.getTime().isBefore(currentSupport.getTime())) {
            // go backward to the support and find the highest candle, it's support
            Long supportCandle = currentSupport.getCandle().getId();
            Candle highestCandle = candleRepository.findHighest(candlePeriod, supportCandle, newAvg.getCandle().getId());
            Resistance newResistance = createResistance(highestCandle, resistancePeriod);
            newAvg.setResistance(newResistance.getCandle());
        }
        averageRepository.save(newAvg);
    }

    private boolean hasNewSupport(Support.Period period, Average newAvg) {
        return newAvg.getSlope() != null
                && getPip(newAvg.getSlope()) >= period.getSlope();
    }

    private boolean hasNewResistance(Resistance.Period period, Average newAvg) {
        return newAvg.getSlope() != null
                && getPip(newAvg.getSlope()) <= period.getSlope();
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
