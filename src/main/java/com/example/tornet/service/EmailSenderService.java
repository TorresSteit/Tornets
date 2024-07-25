package com.example.tornet.service;

import com.example.tornet.model.Customer;
import com.example.tornet.model.Order;
import com.example.tornet.model.Product;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class EmailSenderService {


    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendOrderConfirmationEmail(String to, String orderNumber, String orderDate, List<Product> products, Order order) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Order Confirmation");
            helper.setFrom("taras.royale@gmail.com");

            Context context = new Context();
            context.setVariable("orderNumber", orderNumber);
            context.setVariable("orderDate", orderDate);
            context.setVariable("products", products);
            context.setVariable("order", order); //


            String htmlContent = templateEngine.process("order-confirmation", context);
            helper.setText(htmlContent, true);


            for (Product product : products) {
                String cid = "product" + product.getId();
                DataSource dataSource = getImageDataSource(product.getImage(), "product" + product.getId() + ".jpg");
                helper.addInline(cid, new DataHandler(dataSource).getDataSource());
            }

            javaMailSender.send(message);


        } catch (MessagingException | IOException e) {
            e.printStackTrace();

        }
    }

    private DataSource getImageDataSource(byte[] imageBytes, String fileName) throws IOException {
        return new ByteArrayDataSource(imageBytes, "image/jpeg");
    }


}
