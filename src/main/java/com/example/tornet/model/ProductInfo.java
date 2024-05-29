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


    private String  sizes; // Размеры


    private String  colors; // Цвета


    private String  descriptions; // Описания

@Lob
private byte[] additionalPhotos; // Дополнительные фотографии
    @ManyToOne
    @JoinColumn(name = "product")
private Product product;

}

