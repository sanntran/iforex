package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.Tick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TickRepository extends JpaRepository<Tick, Long> {

}
