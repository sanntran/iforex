package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.repository.AverageRepository;
import net.ionoff.forex.ea.repository.CandleRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Component
@AllArgsConstructor
public class TakeProfitService {

    private AverageRepository averageRepository;

    public void calculateTakeProfit(Average newAvg, String period)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getSlope = Average.class.getMethod(String.format("getSlopeAvg%s", period));
        Method getSupport = Average.class.getMethod(String.format("getSupportCandle%s", period));
        Method getResistance = Average.class.getMethod(String.format("getResistanceCandle%s", period));
        Method setTakeProfit = Average.class.getMethod(String.format("setTakeProfit%s", period), Double.class);

        if (newAvg.getClose() == null) {
            return;
        }
        Double slope = (Double) getSlope.invoke(newAvg);
        if (slope == null) {
            return;
        }
        Candle supportCandle = (Candle) getSupport.invoke(newAvg);
        Candle resistanceCandle = (Candle) getResistance.invoke(newAvg);

        if (supportCandle == null || resistanceCandle == null) {
            return;
        }
        if (slope < 0) { // going down
            Double takeProfit = slope * newAvg.getTime().getEpochSecond()
                    + (supportCandle.getClose() - slope * supportCandle.getTime().getEpochSecond());
            setTakeProfit.invoke(newAvg, takeProfit);
        } else if (slope > 0) {
            Double takeProfit = slope * newAvg.getTime().getEpochSecond()
                    + (resistanceCandle.getClose() - slope * resistanceCandle.getTime().getEpochSecond());
            setTakeProfit.invoke(newAvg, takeProfit);
        }
    }
}
