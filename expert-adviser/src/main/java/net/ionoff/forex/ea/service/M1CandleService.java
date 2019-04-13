package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.model.candle.EurUsdM1Candle;
import net.ionoff.forex.ea.model.Decision;
import net.ionoff.forex.ea.repository.EurUsdM1CandleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Transactional
public class M1CandleService {
    private static final SimpleDateFormat MT4_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    @Autowired
    EurUsdM1CandleRepository eurUsdM1CandleRepository;

    private String toMqlFormat(Decision decision) {
        // TODO Auto-generated method stub
        return null;
    }

    public void saveM1Candle(String symbol, String m1BarTime, double m1BarOpen, double m1BarHigh, double m1BarLow,
                             double m1BarClose, long m1BarVolume) throws ParseException {
        Date m1BarDate = MT4_DATE_FORMAT.parse(m1BarTime);
        EurUsdM1Candle m1Candle = getM1Candle(m1BarDate);
        updateCandle(m1Candle, m1BarOpen, m1BarHigh, m1BarLow, m1BarClose, m1BarVolume, m1BarDate);
        eurUsdM1CandleRepository.save(m1Candle);
    }

    private void updateCandle(EurUsdM1Candle candle, double open, double high, double low, double close,
                              long volume, Date date) {
        candle.setTime(date);
        candle.setLow(low);
        candle.setHigh(high);
        candle.setOpen(open);
        if ((candle.isNew() && close > candle.getOpen())
                || (!candle.isNew() && close > candle.getClose())) {
            candle.setVolBuy(candle.getVolBuy() + 1);
        }
        candle.setClose(close);
        candle.setVolume(volume);
    }

    private EurUsdM1Candle getM1Candle(Date m1BarTime) throws ParseException {
        EurUsdM1Candle m1Candle = eurUsdM1CandleRepository.findByTime(m1BarTime);
        if (m1Candle == null) {
            m1Candle = new EurUsdM1Candle();
        }
        return m1Candle;
    }
}
