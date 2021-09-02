package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandleRepository extends JpaRepository<Candle, Long> {

    @Query(value = "SELECT * FROM candles WHERE period = :period ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Candle> findLatest(@Param("period") String period);

    @Query(value = "SELECT * FROM candles WHERE period = :period ORDER BY id DESC LIMIT :limit",
            nativeQuery = true)
    List<Candle> findLatest(@Param("period") String period, @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM candles WHERE id > :fromId AND period = :period ORDER BY close, id DESC LIMIT 1",
            nativeQuery = true)
    Candle findLowest(@Param("period") String period, @Param("fromId") Long fromId);

    @Query(value = "SELECT * FROM candles WHERE id > :fromId AND id <= :toId AND period = :period ORDER BY close, id DESC LIMIT 1",
            nativeQuery = true)
    Candle findLowest(@Param("period") String period, @Param("fromId") Long fromId, @Param("toId") Long toId);

    @Query(value = "SELECT * FROM candles WHERE id > :fromId AND period = :period ORDER BY close DESC, id DESC LIMIT 1",
            nativeQuery = true)
    Candle findHighest(@Param("period") String period, @Param("fromId") long fromId);

    @Query(value = "SELECT * FROM candles WHERE id > :fromId AND id <= :toId AND period = :period ORDER BY close DESC, id DESC LIMIT 1",
            nativeQuery = true)
    Candle findHighest(@Param("period") String period, @Param("fromId") Long fromId, @Param("toId") Long toId);
}
