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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Component
@AllArgsConstructor
public class AverageService {

    private static final Integer AVERAGE = Average.AVG_LONG_CANDLES;

    private CandleRepository candleRepository;
    private AverageRepository averageRepository;
    private ExtremaService extremaService;
    private TakeProfitService takeProfitService;

    public void updateAverage(Average average,
                              List<Average> averages) {
        try {
            averages.add(0, average);
            calculateAvg(average, averages);
            calculateDistance(average);
            calculateSlopeAvg(average, averages);
            calculateSlopeDistance(average, averages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<Average> findLatest() {
        return averageRepository.findLatest();
    }

    public List<Average> createAverage(List<Candle> candles) {
        Average newAvg = Average.builder()
                .candle(candles.get(0))
                .pivot(candles.stream().mapToDouble(Candle::getPivot).sum()/candles.size())
                .time(candles.get(0).getTime())
                .open(candles.get(candles.size() - 1).getOpen())
                .close(candles.get(0).getClose())
                .build();
        List<Average> averages = averageRepository.findLatest(AVERAGE);
        try {
            updateAverage(newAvg, averages);
            calculateExtrema(newAvg, averages);
            calculateTakeProfit(newAvg, averages);
            averageRepository.save(newAvg);
            return averages;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private void calculateTakeProfit(Average newAvg, List<Average> averages)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        takeProfitService.calculateTakeProfit(newAvg, "Medium");
        takeProfitService.calculateTakeProfit(newAvg, "Long");
    }

    private void calculateExtrema(Average newAvg, List<Average> averages) throws Exception {
        extremaService.calculateSupportAndResistanceShort(newAvg, averages);
        extremaService.calculateSupportAndResistanceMedium(newAvg, averages);
        extremaService.calculateSupportAndResistanceLong(newAvg, averages);
    }

    private void calculateAvg(Average newAvg, List<Average> averages) throws Exception {
        calculateAvg(newAvg, averages, Average.AVG_SHORT_CANDLES,"Short");
        calculateAvg(newAvg, averages, Average.AVG_MEDIUM_CANDLES,"Medium");
        calculateAvg(newAvg, averages, Average.AVG_LONG_CANDLES, "Long");
    }

    private void calculateAvg(Average newAvg, List<Average> averages, int points, String period) throws Exception {
        if (averages.size() >= points) {
            calculateAvg(newAvg, averages.subList(0, points), period);
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
        calculateSlope(newAvg, averages, Average.SLOPE_AVG_SHORT_CANDLES, "AvgShort", "AvgShort");
        calculateSlope(newAvg, averages, Average.SLOPE_AVG_MEDIUM_CANDLES, "AvgMedium", "AvgMedium");
        calculateSlope(newAvg, averages, Average.SLOPE_AVG_LONG_CANDLES, "AvgLong", "AvgLong");
    }

    private void calculateSlopeDistance(Average newAvg, List<Average> averages) throws Exception {
        calculateSlope(newAvg, averages, Average.SLOPE_DISTANCE_AVG_SHORT_AVG_LONG_CANDLES,
                "DistanceAvgShortAvgLong", "DistanceAvgShortAvgLong");
        calculateSlope(newAvg, averages, Average.SLOPE_DISTANCE_AVG_MEDIUM_AVG_LONG_CANDLES,
                "DistanceAvgMediumAvgLong", "DistanceAvgMediumAvgLong");
    }

    private void calculateSlope(Average newAvg, List<Average> averages, int points, String period, String data) throws Exception {
        if (averages.size() >= points) {
            calculateSlope(newAvg, averages.subList(0, points), period, data);
        }
    }
    
    private void calculateAvg(Average newAvg, List<Average> averages, String period) throws Exception {
        Method setAvg = Average.class.getMethod(String.format("setAvg%s", period), Double.class);
        double avg = averages
                .stream()
                .mapToDouble(Average::getPivot)
                .sum() / averages.size();
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
