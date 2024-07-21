package com.example.tornet;

import com.example.tornet.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testPersistOrder() {
        Order order = new Order();
        order.setCreateOrder(new Date());
        // Установите другие необходимые поля...

        Order savedOrder = entityManager.persistAndFlush(order);

        Order foundOrder = entityManager.find(Order.class, savedOrder.getId());
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(savedOrder.getId());
    }


}

