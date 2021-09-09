package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM orders WHERE ticket = :ticket ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Order> findByTicket(@Param("ticket") Long ticket);


    @Query(value = "SELECT * FROM orders WHERE close_time IS NULL AND close_price IS NULL ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Order> findOpen();
}
