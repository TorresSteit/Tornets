package com.example.tornet.service;

import com.example.tornet.model.Order;
import com.example.tornet.model.Product;
import com.example.tornet.reposotory.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
    }

    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllWithDetails();
    }

    public List<Product> getProductsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            return order.getProducts();
        } else {
            return Collections.emptyList(); // or handle null case appropriately
        }
    }

    public Order findById(Long orderId) {
        return  orderRepository.findById(orderId).orElse(null);
    }



    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
}

