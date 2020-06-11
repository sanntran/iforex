package net.ionoff.forex.ea.service;

import net.ionoff.forex.ea.entity.CandleV300;
import net.ionoff.forex.ea.repository.CandleV300Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.*;

@Component
@Transactional
public class CandleV300Service {

    @Autowired
    CandleV300Repository candleV300Repository;

    public void importV300Candles() {
        int volume = 300;
        String tickDataCsvFile = "C:\\Program Files (x86)\\Tickstory\\EURUSD.csv";

        Instant instant = OffsetDateTime.parse("2018-01-01T22:00:00+00:00").toInstant();

        CandleV300 candle = new CandleV300();
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
                        candleV300Repository.save(candle);
                        writer.write(candle.toMt4CsvLine());
                        instant = instant.plus(Duration.ofMinutes(5));
                        candle = new CandleV300();
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
