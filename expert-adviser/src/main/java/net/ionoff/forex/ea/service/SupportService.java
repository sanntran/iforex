package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.*;
import net.ionoff.forex.ea.repository.AverageRepository;
import net.ionoff.forex.ea.repository.CandleRepository;
import net.ionoff.forex.ea.repository.ResistanceRepository;
import net.ionoff.forex.ea.repository.SupportRepository;
import org.springframework.stereotype.Component;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Component
@AllArgsConstructor
public class SupportService {

    private ExtremaService extremaService;
    private CandleRepository candleRepository;
    private AverageRepository averageRepository;

    public void updateSupport(Average average, Prediction prediction) {
        if (average.getClose() == null || average.getSlope() == null) {
            return;
        }
        final String candlePeriod = Candle.Period.SHORT.name();
        final Support.Period supportPeriod = Support.Period.valueOf(average.getPeriod().name());
        final Resistance.Period resistancePeriod = Resistance.Period.valueOf(average.getPeriod().name());
        final Support currentSupport = extremaService.getOrCreateSupport(average.getCandle(), supportPeriod);
        final Resistance currentResistance = extremaService.getOrCreateResistance(average.getCandle(), resistancePeriod);

        if (hasNewSupport(supportPeriod, currentSupport, currentResistance, prediction)) {
            // go backward to the resistance and find the lowest candle, it's support
            Candle lowestCandle = findLowestCandleFromResistance(candlePeriod, currentResistance, average);;
            Support newSupport = extremaService.createSupport(lowestCandle, supportPeriod);
            average.setSupport(newSupport.getCandle());
        } else {
            average.setSupport(currentSupport.getCandle());
        }
        averageRepository.save(average);
    }

    private Candle findLowestCandleFromResistance(String candlePeriod, Resistance currentResistance, Average average) {
        return candleRepository.findLowest(candlePeriod,
                currentResistance.getCandle().getId(), average.getCandle().getId());
    }

    private boolean hasNewSupport(Support.Period period,
                                    Support currentSupport,
                                     Resistance currentResistance,
                                     Prediction prediction) {
        return currentSupport.getTime().isBefore(currentResistance.getTime())
                && prediction.getSlope() != null
                && Slope.of(prediction.getSlope(), period.name()).asReadableSlope() >= period.getSlope();
    }

}
