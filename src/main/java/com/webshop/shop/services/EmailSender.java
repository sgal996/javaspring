package com.webshop.shop.services;

import com.webshop.shop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String recipient) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setText("Vaša narudžba je zaprimljena! \n Dobit ćete potvrdu narudžbe u najbržem mogućem roku! \n Vaš ClothesShop!");
        mailSender.send(message);
    }
    public void sendConfirmationMail(String recipient, List<Product>products){
        SimpleMailMessage message = new SimpleMailMessage();
        String poruka = new String("Vaša narudžba je potvrđena te će Vam biti poslana u najkraćem mogućem roku! Sadržaj narudžbe: \n");
        BigDecimal price = new BigDecimal(0);
        for (Product product: products) {
            poruka += product.getName();
            poruka += " ";
            poruka += product.getPrice().subtract(product.getPrice().multiply(product.getDiscount())).toString();
            poruka += "\n";
            price = price.add(product.getPrice().subtract(product.getPrice().multiply(product.getDiscount())));

        }
        poruka += "Ukupno: ";
        poruka += price.toString();
        poruka += "HRK";
        message.setText(poruka);
        message.setTo(recipient);
        mailSender.send(message);


    }
}
