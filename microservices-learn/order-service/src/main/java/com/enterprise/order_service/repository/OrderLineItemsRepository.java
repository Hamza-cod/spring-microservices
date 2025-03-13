package com.enterprise.order_service.repository;

import com.enterprise.order_service.model.OrderLineItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemsRepository extends JpaRepository<OrderLineItems,Long> {
}
