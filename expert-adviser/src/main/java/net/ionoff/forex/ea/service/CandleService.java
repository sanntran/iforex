package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.event.CandleClosedEvent;
import net.ionoff.forex.ea.event.CandleEventNotifier;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.Message;
import net.ionoff.forex.ea.repository.CandleRepository;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CandleService {

    private CandleRepository candleRepository;
    private CandleEventNotifier candleEventNotifier;

    public Message createCandle(Candle candle) {
        candle.setPivot((candle.getOpen() + candle.getLow() + candle.getHigh())/3);
        Optional<Candle> prevCandle = candleRepository.findLatest();
        prevCandle.ifPresent(candle1 -> candle.setTime(candle1.getTime().plus(Duration.ofHours(1))));
        candleRepository.save(candle);
        candleEventNotifier.fireCandleEvent(new CandleClosedEvent(candle));
        return Message.candleClosed();
    }

    private void mergeCandle(Candle candle, Candle minute) {
        if (candle.getVolume() == 0) {
            candle.setTime(minute.getTime());
            candle.setHigh(minute.getHigh());
            candle.setLow(minute.getLow());
            candle.setOpen(minute.getOpen());
            candle.setClose(minute.getClose());
            candle.setPivot((minute.getLow() + minute.getHigh())/2);
            candle.setVolume(minute.getVolume());
        } else {
            candle.setVolume(candle.getVolume() + minute.getVolume());
            if (minute.getHigh() > candle.getHigh()) {
                candle.setHigh(minute.getHigh());
            }
            if (minute.getLow() < candle.getLow()) {
                candle.setLow(minute.getLow());
            }
            candle.setClose(minute.getClose());
            candle.setPivot((candle.getLow() + candle.getHigh())/2);
        }
    }

    public void exportCandles() throws IOException {
        String csvFile = "F:\\Projects\\forex\\candles.csv";
        File fout = new File(csvFile);
        FileOutputStream fos = new FileOutputStream(fout);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        for (long i = 1; i < 20000; i++) {
            Candle candle = candleRepository.getOne(i);
            osw.write(candle.toMt4CsvLine());
        }
        osw.close();
    }

    private static Instant toInstant(String tickTime) {
        String dateTime = String.format("%s +0000", tickTime.substring(0, tickTime.lastIndexOf(":")));
        return OffsetDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss Z")).toInstant();
    }
}
