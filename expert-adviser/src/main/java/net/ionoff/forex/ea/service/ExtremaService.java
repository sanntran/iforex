package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.repository.AverageRepository;
import net.ionoff.forex.ea.repository.CandleRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Component
@AllArgsConstructor
public class ExtremaService {

    private CandleRepository candleRepository;
    private AverageRepository averageRepository;

    public void calculateSupportAndResistance(Average newAvg, List<Average> averages, String period)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getSlope = Average.class.getMethod(String.format("getSlopeAvg%s", period));
        Method setSupport = Average.class.getMethod(String.format("setSupportClose%s", period), Double.class);
        Method setResistance = Average.class.getMethod(String.format("setResistanceClose%s", period), Double.class);

        if (getSlope.invoke(newAvg) == null) {
            return;
        }
        if (newAvg.getClose() == null || averages.size() < 2) {
            return;
        }
        Average prevAvg = averages.get(1);
        if (getSlope.invoke(prevAvg) == null) {
            return;
        }
        // reuse support and resistance of prev candle
        setSupport.invoke(newAvg, prevAvg.getSupportCloseShort());
        setResistance.invoke(newAvg, prevAvg.getResistanceCloseShort());
        if (newAvg.getResistanceCloseShort() == null) {
            setResistance.invoke(newAvg, newAvg.getClose());
        }
        if (newAvg.getSupportCloseShort() == null) {
            setSupport.invoke(newAvg, newAvg.getClose());
        }
        if ((Double)getSlope.invoke(prevAvg) > 0 && prevAvg.getSlopeShortAvgShort() <= 0) {
            // going up // we have new short support
            // go backward to past
            List<Average> avgListHasExtrema = averages.subList(0, findExtremaAvgIndex(averages, (Double)getSlope.invoke(prevAvg)));
            setSupport.invoke(newAvg, newAvg.getClose());
            for (int i = 1; i < avgListHasExtrema.size(); i++) {
                if (avgListHasExtrema.get(i).getClose() == null) {
                    continue;
                }
                if (avgListHasExtrema.get(i).getClose() < newAvg.getSupportCloseShort()){
                    setSupport.invoke(newAvg, avgListHasExtrema.get(i).getClose());
                }
            }
        } else if ((Double)getSlope.invoke(prevAvg) < 0 && prevAvg.getSlopeShortAvgShort() >= 0) {
            // going down // we have new short resistance
            // go backward to past
            List<Average> avgListHasExtrema = averages.subList(0, findExtremaAvgIndex(averages, (Double)getSlope.invoke(prevAvg)));
            setResistance.invoke(newAvg, newAvg.getClose());
            for (int i = 1; i < avgListHasExtrema.size(); i++) {
                if (avgListHasExtrema.get(i).getClose() == null) {
                    continue;
                }
                if (avgListHasExtrema.get(i).getClose() > newAvg.getResistanceCloseShort()){
                    setResistance.invoke(newAvg, avgListHasExtrema.get(i).getClose());
                }
            }
        }
    }

    private int findExtremaAvgIndex(List<Average> averages, double currentSlope) {
        // go back to the past
        // the prev slope must be different sign
        for (int i = 1; i < averages.size() - 1; i++) {
            if (averages.get(i).getSlopeShortAvgShort() == null) {
                continue;
            }
            if (averages.get(i).getSlopeShortAvgShort() * currentSlope > 0) {
                return i;
            }
        }
        return averages.size() - 1; // use last candle
    }
}
