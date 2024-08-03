package com.example.tornet.configuration;

import com.example.tornet.exception.CustomerNotFoundException;
import com.example.tornet.model.Customer;
import com.example.tornet.model.Role;
import com.example.tornet.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerService.findCustomerByEmail(email);
        if (customer == null) {
            log.error("Customer with email {} not found", email);
            throw new UsernameNotFoundException("Customer with email " + email + " not found");
        }

        List<GrantedAuthority> roles = Arrays.asList(
                new SimpleGrantedAuthority(customer.getRole().toString())
        );

        log.info("User {} has roles: {}", email, roles);
        return new User(customer.getEmail(), customer.getPassword(), roles);
    }
}




