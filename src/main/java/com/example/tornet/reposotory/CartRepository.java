package com.example.tornet.reposotory;

import com.example.tornet.model.Cart;
import com.example.tornet.model.Customer;
import com.example.tornet.model.Order;
import com.example.tornet.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByProductId(Long productId);
    List<Cart> findByCustomer(Customer customer);
    List<Cart> findByOrder(Order order);
    List<Cart> findByProduct(Product product);
    void deleteById(Long cartId);
}

