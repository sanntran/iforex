package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.event.CandleClosedEvent;
import net.ionoff.forex.ea.event.CandleEventNotifier;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.Message;
import net.ionoff.forex.ea.model.Tick;
import net.ionoff.forex.ea.repository.CandleRepository;
import net.ionoff.forex.ea.repository.TickRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TickService {

    private TickRepository tickRepository;
    private CandleRepository candleRepository;
    private CandleEventNotifier candleEventNotifier;
    private static Candle lastCandle = new Candle();

    public Message handleTick(Tick tick) {
        addNewTick(lastCandle, tick);
        if ((lastCandle.getHeight() >= 40
                && Duration.between(lastCandle.getTime(), tick.getTime()).compareTo(Duration.ofSeconds(150)) >= 0)
            || Duration.between(lastCandle.getTime(), tick.getTime()).compareTo(Duration.ofMinutes(5)) >= 0) {
            candleRepository.save(lastCandle);
            candleEventNotifier.fireCandleEvent(new CandleClosedEvent(lastCandle));
            lastCandle = new Candle();
            return Message.ok();
        }
        return Message.ok();
    }

    private Candle createNewCandle(Tick tick) {
        Candle newCandle = new Candle();
        addNewTick(newCandle, tick);
        return candleRepository.save(newCandle);
    }

    public void importTicks() {
        int i = 0;
        String tickDataCsvFile = "C:\\Program Files (x86)\\Tickstory\\EURUSD_20210523_20210528.csv";
        try (BufferedReader fileBufferReader = new BufferedReader(new FileReader(tickDataCsvFile))){
            String line;
            while ((line = fileBufferReader.readLine()) != null) {
                String data[] = line.split(",");
                if ("Timestamp".equalsIgnoreCase(data[0])
                        || "Bid price".equalsIgnoreCase(data[1])
                        || "Ask price".equalsIgnoreCase(data[2])) {
                    // first line
                    continue;
                }
                Tick tick = Tick.builder()
                        .time(toInstant(data[0]))
                        .bid(Double.valueOf(data[1]))
                        .ask(Double.valueOf(data[2]))
                        .build();
                handleTick(tick);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error when import ticks", e);
        }
    }

    private void addNewTick(Candle candle, Tick tick) {
        if (candle.getVolume() == 0) {
            candle.setTime(tick.getTime());
            candle.setHigh(tick.getAsk());
            candle.setLow(tick.getAsk());
            candle.setOpen(tick.getAsk());
            candle.setClose(tick.getAsk());
            candle.setPivot(tick.getAsk());
            candle.setVolume(1);
        } else {
            candle.setVolume(candle.getVolume() + 1);
            if (tick.getAsk() > candle.getHigh()) {
                candle.setHigh(tick.getAsk());
            } else if (tick.getAsk() < candle.getLow()) {
                candle.setLow(tick.getAsk());
            }
            candle.setClose(tick.getAsk());
            candle.setPivot((candle.getOpen() + candle.getLow() + candle.getHigh())/3);
        }
    }

    private static Instant toInstant(String tickTime) {
        String dateTime = String.format("%s +0000", tickTime.substring(0, tickTime.lastIndexOf(":")));
        return OffsetDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss Z")).toInstant();
    }
}
