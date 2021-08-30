package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface CandleRepository extends JpaRepository<Candle, Long> {

    @Query(value = "SELECT * FROM candles WHERE id in (:ids)",
            nativeQuery = true)
    List<Candle> findByIds(@Param("ids") List<Long> ids);

    @Query(value = "SELECT * FROM candles WHERE close = (SELECT MIN(c.close) FROM candles c WHERE c.id > :fromId) ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Candle findLowest(@Param("fromId") Long fromId);

    @Query(value = "SELECT * FROM candles WHERE close = (SELECT MIN(c.close) FROM candles c WHERE c.id > :fromId AND c.id < :toId) ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Candle findLowest(@Param("fromId") Long fromId, @Param("toId") Long toId);

    @Query(value = "SELECT * FROM candles WHERE close = (SELECT MAX(close) FROM candles WHERE id > :fromId) ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Candle findHighest(@Param("fromId") long fromId);

    @Query(value = "SELECT * FROM candles WHERE close = (SELECT MAX(c.close) FROM candles c WHERE c.id > :fromId AND c.id < :toId) ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Candle findHighest(@Param("fromId") Long fromId, @Param("toId") Long toId);

    @Query(value = "SELECT * FROM candles WHERE id = ((SELECT id FROM candles WHERE time >= :fromDateTime AND time <= :toDateTime LIMIT 1) - 1) ",
            nativeQuery = true)
    Optional<Candle> findPrevious(@Param("fromDateTime") Instant fromDateTime, @Param("toDateTime") Instant toDateTime);

    @Query(value = "SELECT * FROM candles WHERE id >= :fromId ORDER BY id DESC",
            nativeQuery = true)
    List<Candle> findFromId(@Param("fromId") long fromId);

    @Query(value = "SELECT * FROM candles WHERE id = (SELECT MAX(id) FROM candles)",
            nativeQuery = true)
    Optional<Candle> findLatest();

    @Query(value = "SELECT * FROM candles WHERE time >=:time ORDER BY time",
            nativeQuery = true)
    List<Candle> findAfterTime(@Param("time") Instant time);

    @Query(value = "SELECT * FROM candles WHERE id >= :fromId AND id <= :toId ORDER BY id DESC",
            nativeQuery = true)
    List<Candle> findFromIdToId(@Param("fromId") long fromId, @Param("toId") long toId);

    @Query(value = "SELECT * FROM candles WHERE period = :period ORDER BY id DESC LIMIT :limit",
            nativeQuery = true)
    List<Candle> findLatest(@Param("period") Candle.Period period, @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM candles WHERE period = :period AND id = (SELECT MAX(id) FROM candles)",
            nativeQuery = true)
    Optional<Candle> findLatest(@Param("period") Candle.Period period);
}
