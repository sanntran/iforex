package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.model.candle.M1Candle;
import net.ionoff.forex.ea.repository.M1CandleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;

@Component
@Transactional
public class M1CandleService {
    @Autowired
    M1CandleRepository m1CandleRepository;

    public void saveM1Candle(String symbol, String barTime, double barOpen, double barHigh, double barLow,
                             double barClose, long barVolume) throws ParseException {
        Instant barInstant = toInstant(barTime);
        M1Candle m1Candle = getM1Candle(barInstant);
        updateCandle(m1Candle, barOpen, barHigh, barLow, barClose, barVolume, barInstant);
        m1CandleRepository.save(m1Candle);
    }

    private void updateCandle(M1Candle candle, double open, double high, double low, double close,
                              long volume, Instant date) {
        candle.setTime(date);
        candle.setLow(low);
        candle.setHigh(high);
        candle.setOpen(open);
        candle.setClose(close);
        candle.setVolume(volume);
    }

    private M1Candle getM1Candle(Instant barDate) throws ParseException {
        M1Candle m1Candle = m1CandleRepository.findByTime(barDate);
        if (m1Candle == null) {
            m1Candle = new M1Candle();
        }
        return m1Candle;
    }

    private static Instant toInstant(String batTime) {
        return Instant.parse(batTime.replace("\\.", "-").replace(" ", "T" + "+00:00"));
    }
}
