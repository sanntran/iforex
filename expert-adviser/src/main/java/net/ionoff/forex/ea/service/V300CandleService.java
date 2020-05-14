package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.model.candle.V300Candle;
import net.ionoff.forex.ea.repository.V300CandleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.*;
import java.util.Calendar;

@Component
@Transactional
public class V300CandleService {

    @Autowired
    V300CandleRepository v300CandleRepository;


    public void importV300Candles() {
        int volume = 300;
        String tickDataCsvFile = "C:\\Program Files (x86)\\Tickstory\\EURUSD.csv";

        Instant instant = OffsetDateTime.parse("2018-01-01T22:00:00+00:00").toInstant();

        V300Candle candle = new V300Candle();
        candle.setTime(Instant.ofEpochMilli(instant.toEpochMilli()));
        candle.setVolume(0);

        try (BufferedReader fileBufferReader = new BufferedReader(new FileReader(tickDataCsvFile));
             Writer writer = new BufferedWriter(new OutputStreamWriter(
                     new FileOutputStream("F:\\Forex\\flat_300.csv"), "utf-8"))
        ){
            String line;

            while ((line = fileBufferReader.readLine()) != null) {
                String data[] = line.split(",");
                if (data[2].equals("Ask price")) { // first line
                    continue;
                }
                double askPrice = Double.valueOf(data[2]);
                if (candle.getVolume() == 0) {
                    candle.setHigh(askPrice);
                    candle.setLow(askPrice);
                    candle.setOpen(askPrice);
                    candle.setClose(askPrice);
                    candle.setVolume(1);
                } else if (candle.getVolume() < volume) {
                    candle.setVolume(candle.getVolume() + 1);
                    if (askPrice > candle.getHigh()) {
                        candle.setHigh(askPrice);
                    } else if (candle.getLow() > askPrice) {
                        candle.setLow(askPrice);
                    }
                    if (candle.getVolume() == volume) {
                        candle.setClose(askPrice);
                        v300CandleRepository.save(candle);
                        writer.write(candle.toMt4CsvLine());
                        instant = instant.plus(Duration.ofMinutes(5));
                        candle = new V300Candle();
                        candle.setTime(Instant.ofEpochMilli(instant.toEpochMilli()));
                        candle.setVolume(0);
                    }
                }
                if (OffsetDateTime.ofInstant(instant, ZoneOffset.UTC).getMonthValue() == 3) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
