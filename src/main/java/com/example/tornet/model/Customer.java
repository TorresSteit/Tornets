package com.example.tornet.model;





import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private  String  firstname;
    private  String  lastname;


    private  String  login;

    private String  phone;


    private  String password;

    @Pattern(regexp = ".*@.*", message = "Email must contain @ symbol")
    private  String  email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String  street;
    private String  city;
    private String  state;
    private String  zip;

    @Column(name = "create_date")
    private LocalDate createDate;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDate.now();
    }



    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Review> reviews;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Cart> user;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery")
    private  Delivery delivery;



}

