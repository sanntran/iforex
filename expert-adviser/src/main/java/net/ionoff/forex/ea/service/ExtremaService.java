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
    private AverageRepository averageRepository;
    private SupportRepository supportRepository;
    private ResistanceRepository resistanceRepository;

    public void calculateSupportAndResistanceShort(Average newAvg, List<Average> averages) {
        if (newAvg.getClose() == null || averages.size() < 6 || newAvg.getSlopeAvgShort() == null) {
            return;
        }
        Support support = supportRepository.findLatestShort().orElseGet(()-> {
            Candle lowestCandle = candleRepository.findLowest(0L, newAvg.getCandle().getId());
            return createSupport(lowestCandle, Support.Period.SHORT);
        });
        Resistance resistance = resistanceRepository.findLatestShort().orElseGet(()-> {
            Candle highestCandle = candleRepository.findHighest(0L, newAvg.getCandle().getId());
            return createResistance(highestCandle, Resistance.Period.SHORT);
        });
        newAvg.setResistanceCandleShort(resistance.getCandle());
        newAvg.setSupportCandleShort(support.getCandle());

        if (getPip(newAvg.getSlopeAvgShort()) >= 0.00236) {
            // go backward to the resistance and find the lowest candle, it's support
            Support newSupport = support;
            if (newSupport.getTime().isAfter(resistance.getTime())) {
                newAvg.setSupportCandleShort(newSupport.getCandle());
                return; // already have support
            } else {
                Long resistanceCandle = resistance.getCandle().getId();
                Candle lowestCandle = candleRepository.findLowest(resistanceCandle, newAvg.getCandle().getId());
                newSupport = createSupport(lowestCandle, Support.Period.SHORT);
                newAvg.setSupportCandleShort(newSupport.getCandle());
            }
        } else if (getPip(newAvg.getSlopeAvgShort()) <= -0.00236) {
            Resistance newResistance = resistance;
            if (resistance.getTime().isAfter(support.getTime())) {
                newAvg.setResistanceCandleShort(newResistance.getCandle());
                return; // already have resistance
            } else {
                Long supportCandle = support.getCandle().getId();
                Candle highestCandle = candleRepository.findHighest(supportCandle, newAvg.getCandle().getId());
                newResistance = createResistance(highestCandle, Resistance.Period.SHORT);
                newAvg.setResistanceCandleShort(newResistance.getCandle());
            }
        }
    }

    public void calculateSupportAndResistanceMedium(Average newAvg, List<Average> averages) {
        if (newAvg.getClose() == null || averages.size() < 6 || newAvg.getSlopeAvgMedium() == null) {
            return;
        }
        Support support = supportRepository.findLatestMedium().orElseGet(()-> {
            Candle lowestCandle = candleRepository.findLowest(0L, newAvg.getCandle().getId());
            return createSupport(lowestCandle, Support.Period.MEDIUM);
        });
        Resistance resistance = resistanceRepository.findLatestMedium().orElseGet(()-> {
            Candle highestCandle = candleRepository.findHighest(0L, newAvg.getCandle().getId());
            return createResistance(highestCandle, Resistance.Period.MEDIUM);
        });
        newAvg.setResistanceCandleMedium(resistance.getCandle());
        newAvg.setSupportCandleMedium(support.getCandle());

        if (getPip(newAvg.getSlopeAvgMedium()) >= 0.00236) {
            // go backward to the resistance and find the lowest candle, it's support
            Support newSupport = support;
            if (newSupport.getTime().isAfter(resistance.getTime())) {
                newAvg.setSupportCandleMedium(newSupport.getCandle());
                return; // already have support
            } else {
                Long resistanceCandle = resistance.getCandle().getId();
                Candle lowestCandle = candleRepository.findLowest(resistanceCandle, newAvg.getCandle().getId());
                newSupport = createSupport(lowestCandle, Support.Period.MEDIUM);
                newAvg.setSupportCandleMedium(newSupport.getCandle());
            }
        } else if (getPip(newAvg.getSlopeAvgMedium()) <= -0.00236) {
            Resistance newResistance = resistance;
            if (resistance.getTime().isAfter(support.getTime())) {
                newAvg.setResistanceCandleMedium(newResistance.getCandle());
                return; // already have resistance
            } else {
                Long supportCandle = support.getCandle().getId();
                Candle highestCandle = candleRepository.findHighest(supportCandle, newAvg.getCandle().getId());
                newResistance = createResistance(highestCandle, Resistance.Period.MEDIUM);
                newAvg.setResistanceCandleMedium(newResistance.getCandle());
            }
        }
    }

    public void calculateSupportAndResistanceLong(Average newAvg, List<Average> averages) {
        if (newAvg.getClose() == null || averages.size() < 6 || newAvg.getSlopeAvgLong() == null) {
            return;
        }
        Support support = supportRepository.findLatestLong().orElseGet(()-> {
            Candle lowestCandle = candleRepository.findLowest(0L, newAvg.getCandle().getId());
            return createSupport(lowestCandle, Support.Period.LONG);
        });
        Resistance resistance = resistanceRepository.findLatestLong().orElseGet(()-> {
            Candle highestCandle = candleRepository.findHighest(0L, newAvg.getCandle().getId());
            return createResistance(highestCandle, Resistance.Period.LONG);
        });
        newAvg.setResistanceCandleLong(resistance.getCandle());
        newAvg.setSupportCandleLong(support.getCandle());

        if (getPip(newAvg.getSlopeAvgLong()) >= 0.00118) {
            // go backward to the resistance and find the lowest candle, it's support
            Support newSupport = support;
            if (newSupport.getTime().isAfter(resistance.getTime())) {
                newAvg.setSupportCandleLong(newSupport.getCandle());
                return; // already have support
            } else {
                Long resistanceCandle = resistance.getCandle().getId();
                Candle lowestCandle = candleRepository.findLowest(resistanceCandle, newAvg.getCandle().getId());
                newSupport = createSupport(lowestCandle, Support.Period.LONG);
                newAvg.setSupportCandleLong(newSupport.getCandle());
            }
        } else if (getPip(newAvg.getSlopeAvgLong()) <= -0.00118) {
            Resistance newResistance = resistance;
            if (resistance.getTime().isAfter(support.getTime())) {
                newAvg.setResistanceCandleLong(newResistance.getCandle());
                return; // already have resistance
            } else {
                Long supportCandle = support.getCandle().getId();
                Candle highestCandle = candleRepository.findHighest(supportCandle, newAvg.getCandle().getId());
                newResistance = createResistance(highestCandle, Resistance.Period.LONG);
                newAvg.setResistanceCandleLong(newResistance.getCandle());
            }
        }
    }

    private Support createSupport(Candle candle, Support.Period period) {
        return supportRepository.save(
                Support.builder()
                        .candle(candle)
                        .time(candle.getTime())
                        .close(candle.getClose())
                        .period(period.name())
                        .build()
        );
    }

    private Resistance createResistance(Candle candle, Resistance.Period period) {
        return resistanceRepository.save(
                Resistance.builder()
                        .candle(candle)
                        .time(candle.getTime())
                        .close(candle.getClose())
                        .period(period.name())
                        .build()
        );
    }
}
