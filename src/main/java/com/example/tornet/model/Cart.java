package com.example.tornet.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@Enumerated(EnumType.STRING)
   private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "user")
    private Customer customer;
    private BigDecimal totalPrice;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    public Map<Product, Integer> getProductMap() {
        return Map.of();
    }

    public void setProductMap(Map<Product, Integer> productMap) {
    }
}
