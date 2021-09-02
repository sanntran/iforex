package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    @Query(value = "SELECT * FROM predictions WHERE period=:period ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Prediction> findLatest(@Param("period") String period);

    @Query(value = "SELECT * FROM predictions WHERE period=:period ORDER BY id DESC LIMIT :limit",
            nativeQuery = true)
    List<Prediction> findLatest(@Param("period") String period, @Param("limit") Integer limit);

}
