package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.Prediction;
import net.ionoff.forex.ea.repository.AverageRepository;
import net.ionoff.forex.ea.repository.PredictionRepository;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Component
@AllArgsConstructor
public class PredictionService {

    private AverageRepository averageRepository;
    private PredictionRepository predictionRepository;

    public void createPrediction(Average average) {
        try {
            Prediction.Period period = Prediction.Period.valueOf(average.getPeriod().name());
            createPrediction(average, period);
        } catch (Exception e) {
            log.error("Error when create prediction. {}", e.getMessage(), e);
        }
    }

    public void createPrediction(Average average, Prediction.Period period) {
        Instant future = average.getTime().plus(period.getDuration());
        List<Average> averages = new ArrayList<>(
                averageRepository.findLatest(period.name(), period.getAvgPoints() - 1));
        if (averages.size() < period.getAvgPoints() - 1) {
            return;
        }
        Average nextAvg = Average.builder()
                .time(future)
                .pivot(predictPivot(averages, future))
                .build();
        averages.add(nextAvg);
        nextAvg.setAverage(calculateAvg(averages));
        nextAvg.setSlope(calculateSlope(averages.subList(0, period.getSlopePoints())));

        Prediction newPrediction = Prediction.builder()
                .time(average.getTime())
                .period(period)
                .pivot(nextAvg.getPivot())
                .average(nextAvg.getAverage())
                .slope(nextAvg.getSlope())
                .slopeSlope(calculateSlopeSlope(averages.subList(0, period.getSlopeSlopePoints())))
                .build();

        predictionRepository.save(newPrediction);
    }

    private Double predictPivot(List<Average> averages, Instant future) {
        if (averages.size() < 2) {
            return null;
        }
        Double a = calculateSlope(averages.subList(0, 2));
        Double x1 = (double) averages.get(0).getTime().getEpochSecond();
        Double x2 = (double) future.getEpochSecond();
        Double y1 = averages.get(0).getPivot();
        return nextValue(y1, a, x1, x2);
    }

    private Double nextValue(int n, Double avg1, Double avg2, Double a1) {
        // avg1 = a1/n + a2/n + ... + an/n = (a1 + a2 + ... + an) / n
        // avg2 = a2/n + a3/n + ... + a(n + 1)/n = (a2 + ... + aN + a(n + 1)) / n
        // => a2/n + ... + an/n = avg1 - a1/n = avg2 - a(n + 1)/n
        // => a(n + 1)/n = avg2 - avg1 + a1/n
        // => a(n + 1) = (avg2 - avg1 + a1/n) * n
        // => a(n + 1) = avg2 * n - avg1 * n + a1
        return avg2 * n - avg1 * n + a1;
    }

    private Double nextValue(Double y1, Double a, Double x1, Double x2) {
        // y1 = a*x1 + b;
        // y2 = a*x1 + b
        // => y1 - a*x1 = y2 - a*x2
        // => y2 = y1 - a*x1 + a*x2
        return y1 - a * x1 + a * x2;
    }

    private Double calculateAvg(List<Average> averages) {
        double avg = averages
                .stream()
                .mapToDouble(Average::getPivot)
                .sum() / averages.size();
        return avg;
    }

    private Double calculateSlope(List<Average> averages) {
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
