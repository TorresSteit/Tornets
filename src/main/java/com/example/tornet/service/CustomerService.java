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



    @Transactional
    public boolean addUser(String email, String passHash, Role role, String firstname, String lastname, String phone, String street, String city, String state, String zip, String s) {
        if (customerRepository.existsByEmail(email)) {
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

        customerRepository.save(customer);

        return true;
    }
@Transactional(readOnly = true)
    public Customer findCustomerByEmail(String email) {
        log.debug("Searching for customer with email: {}", email);
        return customerRepository.findByEmail(email);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    public Customer findById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }


}



