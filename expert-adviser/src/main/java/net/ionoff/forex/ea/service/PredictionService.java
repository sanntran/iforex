package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Prediction;
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
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class PredictionService {

    private AverageService averageService;
    private PredictionRepository predictionRepository;

    public void createPrediction(List<Average> averages) {

        Average average = averages.get(0);
        Instant future = average.getTime().plus(Duration.ofMinutes(60));
        try {
            Average nextAvg = Average.builder()
                    .time(future)
                    .pivot(predictPivot(averages, future))
                    .build();
            averageService.updateAverage(nextAvg, new ArrayList<>(averages));

            Prediction newPrediction = Prediction.builder()
                    .time(future)
                    .pivot(nextAvg.getPivot())
                    .avgShort(nextAvg.getAvgShort())
                    .avgMedium(nextAvg.getAvgMedium())
                    .avgLong(nextAvg.getAvgLong())
                    .slopeAvgShort(nextAvg.getSlopeAvgShort())
                    .slopeAvgMedium(nextAvg.getSlopeAvgMedium())
                    .slopeAvgLong(nextAvg.getSlopeAvgLong())
                    .distanceAvgShortAvgLong(nextAvg.getDistanceAvgShortAvgLong())
                    .distanceAvgMediumAvgLong(nextAvg.getDistanceAvgMediumAvgLong())
                    .slopeDistanceAvgShortAvgLong(nextAvg.getSlopeDistanceAvgShortAvgLong())
                    .slopeDistanceAvgMediumAvgLong(nextAvg.getSlopeDistanceAvgMediumAvgLong())
                    .build();

            predictionRepository.save(newPrediction);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private Double calculateSlope(List<Average> averages) {
        SimpleRegression regression = new SimpleRegression();
        // x: avg
        // y: time
        // y = slope * x + intercept
        for (int i = averages.size() - 1; i >= 0 ; i--) {
            int index = averages.size() - i;
            regression.addData(averages.get(i).getTime().getEpochSecond(), averages.get(i).getPivot());
        }
        return regression.getSlope();
    }

    private Double getMax(Double... prices) {
        return Stream.of(prices)
                .filter(Objects::nonNull)
                .max(Double::compareTo)
                .orElse(null);
    }

    private Double getMin(Double... prices) {
        return Stream.of(prices)
                .filter(Objects::nonNull)
                .min(Double::compareTo)
                .orElse(null);
    }

    // avg1 = a1/n + a2/n + ... + an/n = (a1 + a2 + ... + an) / n
    // avg2 = a2/n + a3/n + ... + a(n + 1)/n = (a2 + ... + aN + a(n + 1)) / n
    // => a2/n + ... + an/n = avg1 - a1/n = avg2 - a(n + 1)/n
    // => a(n + 1)/n = avg2 - avg1 + a1/n
    // => a(n + 1) = (avg2 - avg1 + a1/n) * n
    // => a(n + 1) = avg2 * n - avg1 * n + a1
    private Double predictPriceShort(List<Average> averages, Prediction prediction) {
        if (averages.size() < Average.AVG_SHORT_CANDLES || prediction.getAvgShort() == null) {
            return null;
        }
        int n = Average.AVG_SHORT_CANDLES;
        Double avg2 = prediction.getAvgShort();
        Double avg1 = averages.get(0).getAvgShort();
        Double a1 = averages.get(n - 1).getPivot();
        return avg2 * n - avg1 * n + a1;
    }

    private Double predictPriceMedium(List<Average> averages, Prediction prediction) {
        if (averages.size() < Average.AVG_MEDIUM_CANDLES || prediction.getAvgMedium() == null) {
            return null;
        }
        int n = Average.AVG_MEDIUM_CANDLES;
        Double avg2 = prediction.getAvgMedium();
        Double avg1 = averages.get(0).getAvgMedium();
        Double a1 = averages.get(n - 1).getPivot();
        return avg2 * n - avg1 * n + a1;
    }

    private Double predictPriceLong(List<Average> averages, Prediction prediction) {
        if (averages.size() < Average.AVG_LONG_CANDLES || prediction.getAvgMedium() == null) {
            return null;
        }
        int n = Average.AVG_LONG_CANDLES;
        Double avg2 = prediction.getAvgLong();
        Double avg1 = averages.get(0).getAvgLong();
        Double a1 = averages.get(n - 1).getPivot();
        return avg2 * n - avg1 * n + a1;
    }

    private Double predictAvgLong(Average average, Instant future) {
        if (average.getSlopeAvgLong() == null) {
            return null;
        }
        Double y1 = average.getAvgLong();
        Double a = average.getSlopeAvgLong();
        Double x1 = (double) average.getTime().getEpochSecond();
        Double x2 = (double) future.getEpochSecond();
        return nextValue(y1, a, x1, x2);
    }

    private Double predictAvgMedium(Average average, Instant future) {
        if (average.getSlopeAvgMedium() == null) {
            return null;
        }
        Double y1 = average.getAvgMedium();
        Double a = average.getSlopeAvgMedium();
        Double x1 = (double) average.getTime().getEpochSecond();
        Double x2 = (double) future.getEpochSecond();
        return nextValue(y1, a, x1, x2);
    }

    private Double nextValue(Double y1, Double a, Double x1, Double x2) {
        // y1 = a*x1 + b;
        // y2 = a*x1 + b
        // => y1 - a*x1 = y2 - a*x2
        // => y2 = y1 - a*x1 + a*x2
        return y1 - a * x1 + a * x2;
    }

    private Double predictAvgShort(Average average, Instant future) {
        if (average.getSlopeAvgShort() == null) {
            return null;
        }
        Double a = average.getSlopeAvgShort();
        Double x1 = (double) average.getTime().getEpochSecond();
        Double x2 = (double) future.getEpochSecond();
        Double y1 = average.getAvgShort();
        return nextValue(y1, a, x1, x2);
    }

}
