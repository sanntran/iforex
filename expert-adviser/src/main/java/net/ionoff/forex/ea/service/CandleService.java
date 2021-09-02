package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.event.CandleClosedEvent;
import net.ionoff.forex.ea.event.CandleEventNotifier;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.Message;
import net.ionoff.forex.ea.repository.CandleRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CandleService {

    private CandleRepository candleRepository;
    private CandleEventNotifier candleEventNotifier;

    public Message createCandle(Candle candle) {
        candle.setOpened(candle.getTime());
        candle.setClosed(candle.getTime());
        updateLatestCandle(candle, Candle.Period.SHORT);
        updateLatestCandle(candle, Candle.Period.MEDIUM);
        updateLatestCandle(candle, Candle.Period.LONG);
        return Message.ok();
    }

    private void updateLatestCandle(Candle candle, Candle.Period period) {
        Candle lastCandle = getCandleForUpdate(candle, period);
        mergeCandle(lastCandle, candle);
        candleRepository.save(lastCandle);
        if (lastCandle.isClosed()) {
            candleEventNotifier.fireCandleEvent(new CandleClosedEvent(lastCandle));
        }
    }

    private Candle getCandleForUpdate(Candle candle, Candle.Period period) {
        Optional<Candle> lastCandle = candleRepository.findLatest(period.name());
        if (!lastCandle.isPresent()) {
            return newCandle(candle.getTime(),
                    candle.getTime(),
                    candle.getTime(),
                    period);
        } else if (lastCandle.get().isClosed()) {
            return newCandle(lastCandle.get().getTime().plus(period.getDuration()),
                    lastCandle.get().getTime(),
                    lastCandle.get().getTime(),
                    period);
        }
        return lastCandle.get();
    }

    private Candle newCandle(Instant time,
                             Instant opened,
                             Instant closed,
                             Candle.Period period) {
        Candle candle = new Candle();
        candle.setSize(0);
        candle.setTime(time);
        candle.setOpened(opened);
        candle.setClosed(closed);
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
            latestCandle.setSize(latestCandle.getSize() + 1);
        } else if (latestCandle.getPeriod().isVolumeBased()) {
            latestCandle.setSize(newCandle.getSize() + newCandle.getVolume());
        } else if (latestCandle.getPeriod().isHeightBased()) {
            latestCandle.setSize(newCandle.getHeight());
        }
        latestCandle.setClosed(newCandle.getClosed());
    }
}
