package com.example.tornet.reposotory;

import com.example.tornet.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.customer JOIN FETCH o.productInfos pi JOIN FETCH pi.product")
    List<Order> findAllWithDetails();

}
