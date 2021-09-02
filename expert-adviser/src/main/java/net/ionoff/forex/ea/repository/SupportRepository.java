package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface SupportRepository extends JpaRepository<Support, Long> {

    @Query(value = "SELECT * FROM supports WHERE period=:period ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Support> findLatest(@Param("period") String period);
}
