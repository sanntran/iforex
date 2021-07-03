package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM orders WHERE ticket = :ticket",
            nativeQuery = true)
    Optional<Order> findByTicket(@Param("ticket") Long ticket);


    @Query(value = "SELECT * FROM orders WHERE id =(SELECT MAX(id) FROM orders)",
            nativeQuery = true)
    Optional<Order> findLatest();
}
