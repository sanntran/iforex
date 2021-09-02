package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.repository.AverageRepository;
import net.ionoff.forex.ea.repository.CandleRepository;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class AverageService {

    private CandleRepository candleRepository;
    private AverageRepository averageRepository;

    public Optional<Average> createAverage(Candle candle) {
        Optional<Average> average = newAverage(candle);
        if (average.isPresent()) {
            try {
                Average avg = averageRepository.save(average.get());
                return Optional.of(avg);
            } catch (Exception e) {
                log.error("Error when create average. {}", e.getMessage(), e);
            }
        }
        return Optional.empty();
    }

    public Optional<Average> newAverage(Candle candle) {
        Average.Period period = Average.Period.valueOf(candle.getPeriod().name());
        Average newAvg = Average.builder()
                .candle(candle)
                .period(period)
                .pivot(candle.getPivot())
                .time(candle.getTime())
                .open(candle.getOpen())
                .close(candle.getClose())
                .build();
        List<Candle> candles = candleRepository.findLatest(candle.getPeriod().name(), period.getAvgPoints());
        if (candles.size() < period.getAvgPoints()) {
            return Optional.empty();
        }
        newAvg.setAverage(calculateAvg(candles));
        List<Average> averages = new ArrayList<>(
                averageRepository.findLatest(period.name(), period.getSlopePoints() - 1));
        averages.add(newAvg);
        if (averages.size() < period.getSlopePoints()) {
            return Optional.of(newAvg);
        }
        newAvg.setSlope(calculateSlopeAvg(averages));
        if (averages.size() < period.getSlopeSlopePoints()) {
            return Optional.of(newAvg);
        }
        calculateSlopeSlope(averages.subList(0, period.getSlopeSlopePoints()));
        return Optional.of(newAvg);
    }

    private Double calculateAvg(List<Candle> candles) {
        double avg = candles
                .stream()
                .mapToDouble(Candle::getPivot)
                .sum() / candles.size();
        return avg;
    }

    private Double calculateSlopeAvg(List<Average> averages) {
        if (averages.stream().anyMatch(a -> a.getAverage() == null)) {
            return null;
        }
        SimpleRegression regression = new SimpleRegression();
        // x: avg, y: time, y = slope * x + intercept
        for (int i = averages.size() - 1; i >= 0 ; i--) {
            regression.addData(averages.get(i).getTime().getEpochSecond(), averages.get(i).getAverage());
        }
        return regression.getSlope();
    }

    private Double calculateSlopeSlope(List<Average> averages) {
        if (averages.stream().anyMatch(a -> a.getSlope() == null)) {
            return null;
        }
        SimpleRegression regression = new SimpleRegression();
        // x: avg, y: time, y = slope * x + intercept
        for (int i = averages.size() - 1; i >= 0 ; i--) {
            regression.addData(averages.get(i).getTime().getEpochSecond(), averages.get(i).getSlope());
        }
        return regression.getSlope();
    }
}
