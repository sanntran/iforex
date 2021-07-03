package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.repository.AverageRepository;
import net.ionoff.forex.ea.repository.CandleRepository;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Component
@AllArgsConstructor
public class AverageService {

    private static final Integer CANDLE = 288;
    private static final Integer AVERAGE = 288;

    private AverageRepository averageRepository;
    private CandleRepository candleRepository;
    private ExtremaService extremaService;

    public void createAverage(Candle candle) {
        Average newAvg = Average.builder()
                .candle(candle.getId())
                .time(candle.getTime())
                .pivot(candle.getPivot())
                .close(candle.getClose())
                .build();
        List<Candle> candles = candleRepository.findFromIdToId(candle.getId() - CANDLE + 1, candle.getId());
        List<Average> averages = averageRepository.findFromCandleIdToCandleId(candle.getId() - AVERAGE + 1, candle.getId());
        try {
            averages.add(0, newAvg);
            calculateAvg(newAvg, candles);
            calculateDistance(newAvg);
            calculateSlopeAvg(newAvg, averages);
            calculateSlopeSlopeAvg(newAvg, averages);
            calculateSlopeDistance(newAvg, averages);

            averageRepository.save(newAvg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void calculateExtrema(Average newAvg, List<Average> averages) throws Exception {
        calculateExtrema(newAvg, averages, 6,"Short");
        calculateExtrema(newAvg, averages, 24,"Medium");
        calculateExtrema(newAvg, averages, 288, "Long");
    }

    private void calculateExtrema(Average newAvg, List<Average> averages, int points, String period) throws Exception {
        if (averages.size() >= points) {
            extremaService.calculateSupportAndResistance(newAvg, averages, period);
        }
    }

    private void calculateAvg(Average newAvg, List<Candle> candles) throws Exception {
        calculateAvg(newAvg, candles, 6,"Short");
        calculateAvg(newAvg, candles, 24,"Medium");
        calculateAvg(newAvg, candles, 288, "Long");
    }

    private void calculateAvg(Average newAvg, List<Candle> candles, int points, String period) throws Exception {
        if (candles.size() >= points) {
            calculateAvg(newAvg, candles.subList(0, points), period);
        }
    }

    private void calculateDistance(Average newAvg) {
        if (newAvg.getAvgShort() != null && newAvg.getAvgLong() != null) {
            newAvg.setDistanceAvgShortAvgLong(newAvg.getAvgShort() - newAvg.getAvgLong());
        }
        if (newAvg.getAvgMedium() != null && newAvg.getAvgLong() != null) {
            newAvg.setDistanceAvgMediumAvgLong(newAvg.getAvgMedium() - newAvg.getAvgLong());
        }
    }

    private void calculateSlopeAvg(Average newAvg, List<Average> averages) throws Exception {
        calculateSlope(newAvg, averages, 24, "AvgShort", "AvgShort");
        calculateSlope(newAvg, averages, 6, "ShortAvgShort", "AvgShort");

        calculateSlope(newAvg, averages, 48, "AvgMedium", "AvgMedium");
        calculateSlope(newAvg, averages, 24, "ShortAvgMedium", "AvgMedium");

        calculateSlope(newAvg, averages, 288, "AvgLong", "AvgLong");
        calculateSlope(newAvg, averages, 48, "ShortAvgLong", "AvgLong");
    }

    private void calculateSlopeSlopeAvg(Average newAvg, List<Average> averages) throws Exception {
        calculateSlope(newAvg, averages, 6, "SlopeAvgMedium", "SlopeAvgMedium");
    }

    private void calculateSlopeDistance(Average newAvg, List<Average> averages) throws Exception {
        calculateSlope(newAvg, averages, 6, "DistanceAvgShortAvgLong", "DistanceAvgShortAvgLong");
        calculateSlope(newAvg, averages, 24, "DistanceAvgMediumAvgLong", "DistanceAvgMediumAvgLong");

        calculateSlope(newAvg, averages, 6, "ShortDistanceAvgShortAvgLong", "DistanceAvgShortAvgLong");
        calculateSlope(newAvg, averages, 24, "ShortDistanceAvgMediumAvgLong", "DistanceAvgMediumAvgLong");
    }

    private void calculateSlope(Average newAvg, List<Average> averages, int points, String period, String data) throws Exception {
        if (averages.size() >= points) {
            calculateSlope(newAvg, averages.subList(0, points), period, data);
        }
    }
    
    private void calculateAvg(Average newAvg, List<Candle> candles, String period) throws Exception {
        Method setAvg = Average.class.getMethod(String.format("setAvg%s", period), Double.class);
        double avg = candles
                .stream()
                .mapToDouble(Candle::getPivot)
                .sum() / candles.size();
        setAvg.invoke(newAvg, avg);
    }

    private void calculateSlope(Average newAvg, List<Average> averages, String period, String data)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getData = Average.class.getMethod(String.format("get%s", data));
        Method setSlope = Average.class.getMethod(String.format("setSlope%s", period), Double.class);
        //Method setSquareError = Average.class.getMethod(String.format("setSquareErrorAvg%s", period), Double.class);
        if (averages.stream()
                .anyMatch(a -> {
                    try {
                        return getData.invoke(a) == null;
                    } catch (Exception e) {
                        return false;
                    }
                })) {
            return;
        }
        SimpleRegression regression = new SimpleRegression();
        // x: avg
        // y: time
        // y = slope * x + intercept
        for (int i = averages.size() - 1; i >= 0 ; i--) {
            regression.addData(averages.get(i).getTime().getEpochSecond(), (Double) getData.invoke(averages.get(i)));
        }
        setSlope.invoke(newAvg, regression.getSlope());
        //setSquareError.invoke(newAvg, regression.getMeanSquareError());
    }
}
