package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.model.CandleV300;
import net.ionoff.forex.ea.model.PatternV300;
import net.ionoff.forex.ea.repository.CandleV300Repository;
import net.ionoff.forex.ea.repository.PatternV300Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PatternV300Service {

    // 1lot (10 dollar per 1 pip)
    static final int DISTANCE_UNIT = 100000;

    @Autowired
    PatternV300Repository patternV300Repository;

    @Autowired
    CandleV300Repository candleV300Repository;

    public void buildData() {
        for (long i = 16; i < 16725; i++) {

            List<CandleV300> candleList = candleV300Repository.findByIds(
                    Arrays.asList(i, i - 1, i - 2, i - 3, i - 15, i + 1, i + 2, i + 3));
            CandleV300 candle = findById(candleList, i);
            CandleV300 last1Candle = findById(candleList, i - 1);
            CandleV300 last2Candle = findById(candleList, i - 2);
            CandleV300 last3Candle = findById(candleList, i - 3);
            CandleV300 lastHighestCandle = candleV300Repository.findHighest(i - 15, i - 3);
            CandleV300 lastLowestCandle = candleV300Repository.findLowest(i - 15, i - 3);
            CandleV300 last15Candle = findById(candleList, i - 15);
            CandleV300 next1Candle = findById(candleList, i + 1);
            CandleV300 next2Candle = findById(candleList, i + 2);
            CandleV300 next3Candle = findById(candleList, i + 3);

            PatternV300 distance = new PatternV300();

            distance.setId(candle.getId());
            distance.setLast1candlePrice(getDistance(last1Candle.getClose(), candle.getClose()));
            distance.setLast2candlePrice(getDistance(last2Candle.getClose(), candle.getClose()));
            distance.setLast3candlePrice(getDistance(last3Candle.getClose(), candle.getClose()));
            distance.setLastHighestPrice(getDistance(lastHighestCandle.getClose(), candle.getClose()));
            distance.setLastHighestCandle(candle.getId() - lastHighestCandle.getId());
            distance.setLastLowestPrice(getDistance(lastLowestCandle.getClose(), candle.getClose()));
            distance.setLastLowestCandle(candle.getId() - lastLowestCandle.getId());
            distance.setLast15candlePrice(getDistance(last15Candle.getClose(), candle.getClose()));
            distance.setNext1candlePrice(getDistance(candle.getClose(), next1Candle.getClose()));
            distance.setNext2candlePrice(getDistance(candle.getClose(), next2Candle.getClose()));
            distance.setNext3candlePrice(getDistance(candle.getClose(), next3Candle.getClose()));

            patternV300Repository.save(distance);
        }
    }

    private CandleV300 findById(List<CandleV300> candleList, long id) {
        for (CandleV300 candle : candleList) {
            if (candle.getId() == id) {
                return candle;
            }
        }
        return null;
    }

    private double getDistance(double from, double to) {
        return Double.valueOf(Math.round((to - from) * DISTANCE_UNIT));
    }
}
