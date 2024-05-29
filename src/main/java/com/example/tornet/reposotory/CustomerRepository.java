package com.example.tornet.reposotory;

import com.example.tornet.model.Customer;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


  Customer findByEmail(String email);
  List<Customer> findAll();
  boolean existsByEmail(String email);




}
