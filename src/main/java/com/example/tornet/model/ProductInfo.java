package com.example.tornet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String  sizes;


    private String  descriptions;
    private  int quantity;

    @Lob
    private byte[] additionalPhoto1;

    @Lob
    private byte[] additionalPhoto2;
    @ManyToOne
    @JoinColumn(name = "product")
private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

}

