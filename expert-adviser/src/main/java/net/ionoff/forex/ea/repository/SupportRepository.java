package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface SupportRepository extends JpaRepository<Support, Long> {
    @Query(value = "SELECT * FROM supports WHERE period = 'SHORT' ORDER BY time DESC LIMIT 1",
            nativeQuery = true)
    Optional<Support> findLatestShort();

    @Query(value = "SELECT * FROM supports WHERE id < (SELECT MAX(id) FROM supports WHERE period = 'SHORT') "
            + "AND period = 'SHORT' ORDER BY time DESC LIMIT 1",
            nativeQuery = true)
    Optional<Support> findPreviousShort();

    @Query(value = "SELECT * FROM supports WHERE period = 'MEDIUM' ORDER BY time DESC LIMIT 1",
            nativeQuery = true)
    Optional<Support> findLatestMedium();

    @Query(value = "SELECT * FROM supports WHERE id < (SELECT MAX(id) FROM supports WHERE period = 'MEDIUM') "
            + "AND period = 'MEDIUM' ORDER BY time DESC LIMIT 1",
            nativeQuery = true)
    Optional<Support> findPreviousMedium();

    @Query(value = "SELECT * FROM supports WHERE period = 'LONG' ORDER BY time DESC LIMIT 1",
            nativeQuery = true)
    Optional<Support> findLatestLong();

    @Query(value = "SELECT * FROM supports "
            + "WHERE id < (SELECT MAX(id) FROM supports WHERE period = 'LONG') "
            + "AND period = 'LONG' ORDER BY time DESC LIMIT 1",
            nativeQuery = true)
    Optional<Support> findPreviousLong();
}
