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
        Optional<Candle> lastCandle = candleRepository.findLatest();
        lastCandle.ifPresent(c -> candle.setTime(c.getTime().plus(Duration.ofMinutes(5))));
        candle.setPivot((candle.getOpen() + candle.getLow() + candle.getHigh())/3);
        candleRepository.save(candle);
        candleEventNotifier.fireCandleEvent(new CandleClosedEvent(candle));
        return Message.ok();
    }

    private Candle newCandle(Instant time) {
        Candle candle = new Candle();
        candle.setTime(time);
        return candle;
    }

    private void mergeCandle(Candle candle, Candle m1) {
        if (candle.getVolume() == 0) {
            candle.setHigh(m1.getHigh());
            candle.setLow(m1.getLow());
            candle.setOpen(m1.getOpen());
            candle.setClose(m1.getClose());
            candle.setPivot((candle.getOpen() + candle.getLow() + candle.getHigh())/3);
            candle.setVolume(m1.getVolume());
        } else {
            candle.setVolume(candle.getVolume() + m1.getVolume());
            if (m1.getHigh() > candle.getHigh()) {
                candle.setHigh(m1.getHigh());
            }
            if (m1.getLow() < candle.getLow()) {
                candle.setLow(m1.getLow());
            }
            candle.setClose(m1.getClose());
            candle.setPivot((candle.getOpen() + candle.getLow() + candle.getHigh())/3);
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
