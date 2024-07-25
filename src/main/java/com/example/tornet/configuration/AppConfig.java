package com.example.tornet.configuration;

import com.example.tornet.model.Customer;
import com.example.tornet.model.Role;
import com.example.tornet.service.CustomerService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class AppConfig {

    public static final String ADMIN_EMAIL = "taras.royale@gmail.com";

    @Bean
    public CommandLineRunner demo(final CustomerService customerService,
                                  final PasswordEncoder encoder) {
        return args -> {

            Customer adminCustomer = new Customer();
            adminCustomer.setEmail(ADMIN_EMAIL);
            adminCustomer.setPassword(encoder.encode("1"));
            adminCustomer.setRole(Role.Admin);
            log.info("Admin customer: " + adminCustomer);

            customerService.save(adminCustomer);


            Customer userCustomer = new Customer();
            userCustomer.setEmail("user@example.com");
            userCustomer.setPassword(encoder.encode("2"));
            userCustomer.setRole(Role.Users);
            log.info("User customer: " + userCustomer);

            customerService.save(userCustomer);
        };
    }
}


