package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.slope.V300Slope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface V300SlopeRepository extends JpaRepository<V300Slope, Long> {


}
