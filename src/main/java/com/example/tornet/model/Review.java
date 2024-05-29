package com.example.tornet.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String  commets;

    private LocalDate create_coments;
    private int likes;


    public void likes(){
        if (likes<=5){
        }
        else {
            System.out.println("\"The maximum number of likes has already been reached\"");
        }
    }



}
