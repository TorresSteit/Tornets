package com.example.tornet;

import com.example.tornet.model.Customer;
import com.example.tornet.model.Delivery;
import com.example.tornet.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class   CustomerTest {

    @Test
    public void testCustomerConstructor() {
        Customer customer = new Customer();
        Assertions.assertNotNull(customer);
    }

    @Test
    public void testCustomerGettersAndSetters() {
         Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("John");
        customer.setLastname("Doe");

        customer.setPhone("123456789");
        customer.setPassword("password");
        customer.setEmail("john.doe@example.com");
        customer.setRole(Role.Users);



        Delivery delivery = new Delivery();
        customer.setDelivery(delivery);

        Assertions.assertEquals(1L, customer.getId());
        Assertions.assertEquals("John", customer.getFirstname());
        Assertions.assertEquals("Doe", customer.getLastname());

        Assertions.assertEquals("123456789", customer.getPhone());
        Assertions.assertEquals("password", customer.getPassword());
        Assertions.assertEquals("john.doe@example.com", customer.getEmail());
        Assertions.assertEquals(Role.Users, customer.getRole());

    }


}

