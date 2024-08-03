package com.example.tornet;

import com.example.tornet.model.Customer;
import com.example.tornet.model.Delivery;
import com.example.tornet.model.Role;
import com.example.tornet.reposotory.CustomerRepository;
import com.example.tornet.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Transactional
    public void testAddUser() {
        String email = "test@example.com";
        String passHash = "hashedPassword";
        Role role = Role.Users;
        String firstname = "Test";
        String lastname = "User";
        String phone = "1234567890";
        String street = "123 Test St";
        String city = "Test City";
        String state = "TS";
        String zip = "12345";

        boolean result = customerService.addUser(email, passHash, role, firstname, lastname, phone, street, city, state, zip);
        assertTrue(result, "User should be added successfully");

        Customer customer = customerRepository.findByEmail(email);
        assertNotNull(customer, "Customer should be found");
        assertEquals(email, customer.getEmail(), "Emails should match");
    }
}




