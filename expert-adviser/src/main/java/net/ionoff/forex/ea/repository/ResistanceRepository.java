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
    @Query(value = "SELECT * FROM resistances WHERE period = 'SHORT' ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Resistance> findLatestShort();

    @Query(value = "SELECT * FROM resistances WHERE id < (SELECT MAX(id) FROM resistances WHERE period = 'SHORT') "
            + "AND period = 'SHORT' ORDER BY time DESC LIMIT 1",
            nativeQuery = true)
    Optional<Resistance> findPreviousShort();

    @Query(value = "SELECT * FROM resistances WHERE period = 'MEDIUM' ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Resistance> findLatestMedium();

    @Query(value = "SELECT * FROM resistances WHERE id < (SELECT MAX(id) FROM resistances WHERE period = 'MEDIUM') "
            + "AND period = 'MEDIUM' ORDER BY time DESC LIMIT 1",
            nativeQuery = true)
    Optional<Resistance> findPreviousMedium();

    @Query(value = "SELECT * FROM resistances WHERE period = 'LONG' ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Resistance> findLatestLong();

    @Query(value = "SELECT * FROM resistances "
            + "WHERE id < (SELECT MAX(id) FROM resistances WHERE period = 'LONG') "
            + "AND period = 'LONG' ORDER BY time DESC LIMIT 1",
            nativeQuery = true)
    Optional<Resistance> findPreviousLong();

    @Query(value = "SELECT * FROM resistances WHERE period=:period ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Resistance> findLatest(@Param("period") Resistance.Period period);
}
