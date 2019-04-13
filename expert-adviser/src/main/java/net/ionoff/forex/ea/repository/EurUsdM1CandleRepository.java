package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.candle.EurUsdM1Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EurUsdM1CandleRepository extends JpaRepository<EurUsdM1Candle, Long> {

    @Query("SELECT candle FROM EurUsdM1Candle candle WHERE candle.time = :barTime")
    EurUsdM1Candle findByTime(@Param("barTime") Date barTime);
}
