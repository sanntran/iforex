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
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public class ExportService {

    public static final DateTimeFormatter MT4_CSV_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy.MM.dd,HH:mm").withZone(ZoneOffset.UTC);

    private CandleRepository candleRepository;

    public void exportCandles() throws IOException {
        String csvFile = "F:\\Projects\\forex\\candles.csv";
        File fout = new File(csvFile);
        FileOutputStream fos = new FileOutputStream(fout);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        for (long i = 1; i < 20000; i++) {
            Candle candle = candleRepository.getOne(i);
            osw.write(toMt4CsvLine(candle));
        }
        osw.close();
    }

    private static Instant toInstant(String tickTime) {
        String dateTime = String.format("%s +0000", tickTime.substring(0, tickTime.lastIndexOf(":")));
        return OffsetDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss Z")).toInstant();
    }

    public String toMt4CsvLine(Candle candle) {
        StringBuilder sb = new StringBuilder();
        sb.append(MT4_CSV_DATE_FORMAT.format(candle.getTime()))
                .append(",").append(candle.getOpen())
                .append(",").append(candle.getHigh())
                .append(",").append(candle.getLow())
                .append(",").append(candle.getClose())
                .append(",").append(candle.getVolume())
                .append("\n");
        return sb.toString();
    }
}
