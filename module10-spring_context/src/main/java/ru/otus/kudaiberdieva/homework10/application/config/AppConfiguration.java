package ru.otus.kudaiberdieva.homework10.application.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.otus.kudaiberdieva.homework10.application.repository.ProductRepository;
import ru.otus.kudaiberdieva.homework10.application.repository.ProductRepositoryImpl;
import ru.otus.kudaiberdieva.homework10.application.services.*;

@Configuration
@ComponentScan(basePackages = "ru.otus.kudaiberdieva.homework10.application")
public class AppConfiguration {

    @Bean
    public IOService ioService() {
        return new IOServiceImpl();
    }

    @Bean
    ProductRepository productRepository() {
        return new ProductRepositoryImpl();
    }

    @Bean
    @Scope("prototype")
    public Cart cart() {
        return new CartImpl();
    }

    @Bean
    public ShopService shopService(ProductRepository productRepository, IOService ioService, Cart cart) {
        return new ShopServiceImpl(productRepository, ioService, cart);
    }
}
