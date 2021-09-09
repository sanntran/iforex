package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.*;
import net.ionoff.forex.ea.repository.AverageRepository;
import net.ionoff.forex.ea.repository.CandleRepository;
import net.ionoff.forex.ea.repository.ResistanceRepository;
import net.ionoff.forex.ea.repository.SupportRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Component
@AllArgsConstructor
public class ResistanceService {

    private ExtremaService extremaService;
    private CandleRepository candleRepository;
    private AverageRepository averageRepository;

    public void updateResistance(Average average, Prediction prediction) {
        if (average.getClose() == null
                || average.getAverage() == null
                || prediction.getAverage() == null) {
            return;
        }
        final String candlePeriod = Candle.Period.SHORT.name();
        final Resistance.Period resistancePeriod = Resistance.Period.valueOf(average.getPeriod().name());
        final Support.Period supportPeriod = Support.Period.valueOf(average.getPeriod().name());

        final Support currentSupport = extremaService.getOrCreateSupport(average.getCandle(), supportPeriod);
        final Resistance currentResistance = extremaService.getOrCreateResistance(average.getCandle(), resistancePeriod);

        average.setResistance(currentResistance.getCandle());

        if (hasNewResistance(resistancePeriod, currentSupport, currentResistance, prediction)) {
            // go backward to the support and find the highest candle, it's support
            Candle highestCandle = findHighestCandleFromSupport(candlePeriod, currentSupport, average);
            Resistance newResistance = extremaService.createResistance(highestCandle, resistancePeriod);
            average.setResistance(newResistance.getCandle());
        } else {
            average.setResistance(currentResistance.getCandle());
        }
        averageRepository.save(average);
    }

    private Candle findHighestCandleFromSupport(String candlePeriod, Support currentSupport, Average average) {
        return candleRepository.findHighest(candlePeriod,
                currentSupport.getCandle().getId(), average.getCandle().getId());
    }

    private boolean hasNewResistance(Resistance.Period period,
                                    Support currentSupport,
                                     Resistance currentResistance,
                                     Prediction prediction) {
        return currentResistance.getTime().isBefore(currentSupport.getTime())
                && prediction.getSlope() != null
                && Slope.of(prediction.getSlope(), period.name()).asReadableSlope() <= period.getSlope();
    }

}
