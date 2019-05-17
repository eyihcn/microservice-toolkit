package eyihcn.order.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eyihcn.order.service.entity.Order;

public interface OrderDAO extends JpaRepository<Order, Long> {

}
