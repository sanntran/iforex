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

    @Query(value = "SELECT * FROM averages WHERE period=:period ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Average> findLatest(@Param("period") String period);

    @Query(value = "SELECT * FROM averages WHERE period=:period ORDER BY id DESC LIMIT :limit",
            nativeQuery = true)
    List<Average> findLatest(@Param("period") String period, @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM averages WHERE candle <= :candle AND period=:period ORDER BY id DESC LIMIT 1 OFFSET 1",
            nativeQuery = true)
    Optional<Average> findByCandle(@Param("candle") Long candle, @Param("period") String period);
}
