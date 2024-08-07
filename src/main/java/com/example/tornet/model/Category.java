package com.example.tornet.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"products"})
public class Category {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)

private  Long id;


private  String  title;



@OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
private List<Product> products;


}
