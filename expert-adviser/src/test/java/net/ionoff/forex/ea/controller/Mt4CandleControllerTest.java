package net.ionoff.forex.ea.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

public class Mt4CandleControllerTest {

    private RestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate = new RestTemplate();
    }

    @Test
    public void importEurUsdM1Data() throws Exception {
        try (Stream<String> stream = Files.lines(Paths.get("F:/Forex/2019_03_21_to_2019_04_14_EURUSD_M1.csv"))) {
            stream.forEach(line -> saveEurUsdM1Candle(line));
        } catch (Exception e) {
            throw e;
        }
    }

    private void saveEurUsdM1Candle(String line) {
        String fields[] = line.split(",");
        String date = fields[0] + " " + fields[1] + ":00";
        StringBuilder urlBuilder = new StringBuilder("http://localhost:8080/candles/m1");
        urlBuilder.append("?symbol=eurusd")
                .append("&m1BarTime=").append(date)
                .append("&m1BarOpen=").append(fields[2])
                .append("&m1BarHigh=").append(fields[3])
                .append("&m1BarLow=").append(fields[4])
                .append("&m1BarClose=").append(fields[5])
                .append("&m1BarVolume=").append(fields[6]);
        String s = restTemplate.getForObject(urlBuilder.toString(), String.class);
        System.out.println(s);
    }
}
