package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AverageRepository extends JpaRepository<Average, Long> {

    @Query(value = "SELECT * FROM averages WHERE id =(SELECT MAX(id) FROM averages)",
            nativeQuery = true)
    Optional<Average> findLatest();

    @Query(value = "SELECT * FROM averages ORDER BY id DESC LIMIT :limit",
            nativeQuery = true)
    List<Average> findLatest(@Param("limit") Integer limit);

    @Query(value = "SELECT * FROM averages WHERE time >=:time ORDER BY time",
            nativeQuery = true)
    List<Average> findAfterTime(@Param("time") Instant time);

    @Query(value = "SELECT * FROM averages WHERE candle >= :fromCandleId AND candle <= :toCandleId ORDER BY candle DESC",
            nativeQuery = true)
    List<Average> findFromCandleIdToCandleId(@Param("fromCandleId")long fromCandleId, @Param("toCandleId") long toCandleId);
}
