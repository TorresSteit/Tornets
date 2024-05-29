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
                // Создание администратора
                Customer adminCustomer = new Customer();
                adminCustomer.setEmail("admin@example.com");
                adminCustomer.setPassword(encoder.encode("adminpassword"));
                adminCustomer.setRole(Role.Admin);

                // Сохранение администратора в базе данных
                customerService.save(adminCustomer);

                // Создание пользователя
                Customer userCustomer = new Customer();
                userCustomer.setEmail("user@example.com");
                userCustomer.setPassword(encoder.encode("userpassword"));
                userCustomer.setRole(Role.Users);

                // Сохранение пользователя в базе данных
                customerService.save(userCustomer);
            }
        };
    }
}

