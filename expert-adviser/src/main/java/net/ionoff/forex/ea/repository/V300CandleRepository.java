package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.candle.V300Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface V300CandleRepository extends JpaRepository<V300Candle, Long> {

    @Query("SELECT candle FROM V300Candle candle WHERE candle.time = :barTime")
    V300Candle findByTime(@Param("barTime") Date barTime);

    @Query(value = "SELECT AVG(c.high) FROM eur_usd_v300_candles c " +
            "WHERE c.id >= :fromId AND c.id <= :toId", nativeQuery = true)
    double getAvgHigh(long fromId, long toId);

    @Query(value = "SELECT c.* FROM eur_usd_v300_candles c " +
            "WHERE c.id >= :fromId AND c.id <= :toId " +
            "AND c.high = (SELECT MAX(high) FROM eur_usd_v300_candles WHERE id >= :fromId AND id <= :toId) LIMIT 1", nativeQuery = true)
    V300Candle getHighest(long fromId, long toId);

    @Query(value = "SELECT AVG(c.low) FROM eur_usd_v300_candles c " +
            "WHERE c.id >= :fromId AND c.id <= :toId", nativeQuery = true)
    double getAvgLow(long fromId, long toId);

    @Query(value = "SELECT c.* FROM eur_usd_v300_candles c " +
            "WHERE c.id >= :fromId AND c.id <= :toId " +
            "AND c.low = (SELECT MIN(low) FROM eur_usd_v300_candles WHERE id >= :fromId AND id <= :toId) LIMIT 1", nativeQuery = true)
    V300Candle getLowest(long fromId, long toId);
}
