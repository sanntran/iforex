package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.Resistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ResistanceRepository extends JpaRepository<Resistance, Long> {

    @Query(value = "SELECT * FROM resistances WHERE period=:period ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Resistance> findLatest(@Param("period") String period);

    @Query(value = "SELECT * FROM resistances WHERE candle <= :candle AND period = :period ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Resistance> findByCandle(@Param("candle") Long candle, @Param("period") String period);
}
