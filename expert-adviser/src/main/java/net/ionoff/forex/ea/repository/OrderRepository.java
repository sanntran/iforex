package net.ionoff.forex.ea.repository;

import net.ionoff.forex.ea.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM orders WHERE ticket = (:ticket)",
            nativeQuery = true)
    Order findByTicket(@Param("ticket") Long ticket);
}
