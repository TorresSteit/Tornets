package com.example.tornet.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "delivery")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String mailcompany;


    @OneToOne(mappedBy = "delivery")
    private Customer customer;

@OneToOne(mappedBy = "delivery")
private Order order;
@Enumerated(EnumType.STRING)
private PaymentMethod paymentMethod;

   @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
