package com.example.tornet.configuration;

import com.example.tornet.model.Customer;
import com.example.tornet.model.Role;
import com.example.tornet.service.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppConfig extends GlobalMethodSecurityConfiguration {

    public static final String ADMIN_EMAIL = "admin@example.com";

    @Bean
    public CommandLineRunner demo(final CustomerService customerService,
                                  final PasswordEncoder encoder) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                // Create administrator
                Customer adminCustomer = new Customer();
                adminCustomer.setEmail(ADMIN_EMAIL);
                adminCustomer.setPassword(encoder.encode("123"));
                adminCustomer.setRole(Role.Admin);

                customerService.save(adminCustomer);

                // Create user
                Customer userCustomer = new Customer();
                userCustomer.setEmail("user@example.com");
                userCustomer.setPassword(encoder.encode("user"));
                userCustomer.setRole(Role.Users);

                customerService.save(userCustomer);
            }
        };
    }
}
