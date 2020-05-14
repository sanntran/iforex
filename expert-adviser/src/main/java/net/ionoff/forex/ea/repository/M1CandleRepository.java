package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.candle.M1Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public interface M1CandleRepository extends JpaRepository<M1Candle, Long> {

    @Query("SELECT candle FROM M1Candle candle WHERE candle.time = :barTime")
    M1Candle findByTime(@Param("barTime") Instant barDate);

    @Query(value = "SELECT HOUR(`time`), AVG(volume) FROM eur_usd_m1_candles GROUP BY hour(`time`) ORDER BY `time`", nativeQuery = true)
    List<Object> avgVolumneByHour();

    @Query(value = "SELECT HOUR(`time`), AVG(volume) FROM eur_usd_m1_candles GROUP BY hour(`time`) ORDER BY `time` WHERE `time` BETWEEN :fromDate AND :toDate", nativeQuery = true)
    List<Object> avgVolumneByHour(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}
