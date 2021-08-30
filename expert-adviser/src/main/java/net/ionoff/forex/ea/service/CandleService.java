package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.event.CandleClosedEvent;
import net.ionoff.forex.ea.event.CandleEventNotifier;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.Message;
import net.ionoff.forex.ea.repository.CandleRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@AllArgsConstructor
public class CandleService {

    private CandleRepository candleRepository;
    private CandleEventNotifier candleEventNotifier;

    public Message createCandle(Candle candle) {
        updateLatestCandle(candle, Candle.Period.SHORT);
        updateLatestCandle(candle, Candle.Period.MEDIUM);
        updateLatestCandle(candle, Candle.Period.LONG);
        return Message.ok();
    }

    private void updateLatestCandle(Candle candle, Candle.Period period) {
        Candle lastCandle = candleRepository.findLatest(period)
                .orElse(newCandle(candle.getTime(), period));
        if (!lastCandle.isClosed()) {
            mergeCandle(lastCandle, candle);
            candleRepository.save(candle);
        }
        if (lastCandle.isClosed()) {
            candleEventNotifier.fireCandleEvent(new CandleClosedEvent(lastCandle));
        }
    }

    private Candle newCandle(Instant time, Candle.Period period) {
        Candle candle = new Candle();
        candle.setSize(0);
        candle.setTime(time);
        candle.setInstant(time);
        candle.setPeriod(period);
        return candle;
    }

    private void mergeCandle(Candle latestCandle, Candle newCandle) {
        if (latestCandle.getVolume() == 0) {
            latestCandle.setHigh(newCandle.getHigh());
            latestCandle.setLow(newCandle.getLow());
            latestCandle.setOpen(newCandle.getOpen());
            latestCandle.setClose(newCandle.getClose());
            latestCandle.setPivot((latestCandle.getOpen() + latestCandle.getLow() + latestCandle.getHigh())/3);
            latestCandle.setVolume(newCandle.getVolume());
        } else {
            latestCandle.setVolume(latestCandle.getVolume() + newCandle.getVolume());
            if (newCandle.getHigh() > latestCandle.getHigh()) {
                latestCandle.setHigh(newCandle.getHigh());
            }
            if (newCandle.getLow() < latestCandle.getLow()) {
                latestCandle.setLow(newCandle.getLow());
            }
            latestCandle.setClose(newCandle.getClose());
            latestCandle.setPivot((latestCandle.getOpen() + latestCandle.getLow() + latestCandle.getHigh())/3);
        }
        if (latestCandle.getPeriod().isCandleBased()) {
            newCandle.setSize(newCandle.getSize() + 1);
        } else if (latestCandle.getPeriod().isVolumeBased()) {
            newCandle.setSize(newCandle.getSize() + newCandle.getVolume());
        } else if (latestCandle.getPeriod().isHeightBased()) {
            newCandle.setSize(newCandle.getHeight());
        }
    }
}
