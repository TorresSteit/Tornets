package com.example.tornet.configuration;

import com.example.tornet.model.Customer;
import com.example.tornet.model.Role;
import com.example.tornet.service.CustomerService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class AppConfig extends GlobalMethodSecurityConfiguration {

    public static final String ADMIN_EMAIL = "taras.royale@gmail.com"; // Ensure email is valid

    @Bean
    public CommandLineRunner demo(final CustomerService service, final PasswordEncoder encoder) {
        return args -> {
            service.addUser(ADMIN_EMAIL, encoder.encode("TorJAs27"), Role.Admin, "Admin", "User", "1234567890", "123 Main St", "City", "State", "12345");
            log.info("Admin user created with email: {}", ADMIN_EMAIL);

        };
    }
}


