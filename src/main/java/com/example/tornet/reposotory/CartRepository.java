package com.example.tornet.reposotory;

import com.example.tornet.model.Cart;
import com.example.tornet.model.Customer;
import com.example.tornet.model.Order;
import com.example.tornet.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c JOIN c.productInfos pi WHERE pi.product = :product")
    List<Cart> findByProduct(@Param("product") Product product);

    List<Cart> findByOrder(Order order);

    void deleteById(Long cartId);
    Cart findByCustomer(Customer customer);

    void deleteByCustomer(Customer customer);
    List<Cart> findByCustomerId(Long customerId);
    Optional<Cart> findByCustomerAndOrderIsNull(Customer customer);
}

