package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

}
