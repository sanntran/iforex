package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.entity.CandleV300;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandleV300Repository extends JpaRepository<CandleV300, Long> {

    @Query(value = "SELECT * FROM v300_candles WHERE id in (:ids)",
            nativeQuery = true)
    List<CandleV300> findByIds(@Param("ids") List<Long> ids);

    @Query(value = "SELECT * FROM v300_candles WHERE low = (SELECT MIN(c.low) FROM v300_candles c WHERE c.id >= :fromId AND c.id <= :toId) LIMIT 1",
            nativeQuery = true)
    CandleV300 findLowest(@Param("fromId") Long from, @Param("toId") Long toId);

    @Query(value = "SELECT * FROM v300_candles WHERE high = (SELECT MAX(c.high) FROM v300_candles c WHERE c.id >= :fromId AND c.id <= :toId) LIMIT 1",
            nativeQuery = true)
    CandleV300 findHighest(@Param("fromId") Long from, @Param("toId") Long toId);
}
