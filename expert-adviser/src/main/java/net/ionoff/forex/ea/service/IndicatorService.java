package net.ionoff.forex.ea.service;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.event.CandleEventNotifier;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.repository.CandleRepository;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class IndicatorService {

    private CandleRepository candleRepository;
    private CandleEventNotifier candleEventNotifier;

    private Average getMovingAverage(Candle candle) {
        return new Average();
    }
}
