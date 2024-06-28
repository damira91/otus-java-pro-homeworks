package ru.otus.kudaiberdieva.homework10.application;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.kudaiberdieva.homework10.application.config.AppConfiguration;
import ru.otus.kudaiberdieva.homework10.application.services.ShopService;

public class CartApp {
    public static void main(String[] args) {
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
            ShopService shopService = context.getBean(ShopService.class);
            shopService.startShopping();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
