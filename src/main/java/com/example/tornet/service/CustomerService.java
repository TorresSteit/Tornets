package com.example.tornet.service;

import com.example.tornet.model.Customer;
import com.example.tornet.model.Role;
import com.example.tornet.reposotory.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public Customer findCustomerByEmail(String email) {
        log.debug("Searching for customer with email: {}", email);
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            log.error("Customer with email {} not found", email);
        } else {
            log.info("Customer with email {} found", email);
        }
        return customer;
    }

    @Transactional
    public boolean addUser(String email, String passHash, Role role, String firstname, String lastname, String phone, String street, String city, String state, String zip) {
        log.info("Attempting to add user with email: {}", email);
        try {
            if (customerRepository.existsByEmail(email)) {
                log.warn("User with email {} already exists.", email);
                return false;
            }

            Customer customer = new Customer();
            customer.setEmail(email);
            customer.setPassword(passHash);
            customer.setRole(role);
            customer.setFirstname(firstname);
            customer.setLastname(lastname);
            customer.setPhone(phone);
            customer.setStreet(street);
            customer.setCity(city);
            customer.setState(state);
            customer.setZip(zip);
            customer.setCreateDateCustomer(new Date());

            log.debug("Customer details: {}", customer);

            customerRepository.save(customer);
            log.info("User with email {} successfully added.", email);
            return true;
        } catch (Exception e) {
            log.error("Error adding user with email: {}", email, e);
            return false;
        }
    }
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}





