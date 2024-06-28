package ru.otus.kudaiberdieva.homework10.application.repository;


import ru.otus.kudaiberdieva.homework10.application.entity.Product;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


public class ProductRepositoryImpl implements ProductRepository {

    private final List<Product> products;

    public ProductRepositoryImpl() {
        products = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        products.add(new Product(1L, "product1", 10.00));
        products.add(new Product(2L, "product2", 20.00));
        products.add(new Product(3L, "product3", 30.00));
        products.add(new Product(4L, "product4", 10.00));
        products.add(new Product(5L, "product5", 40.00));
        products.add(new Product(6L, "product6", 50.00));
        products.add(new Product(7L, "product7", 60.00));
        products.add(new Product(8L, "product8", 50.00));
        products.add(new Product(9L, "product9", 30.00));
        products.add(new Product(10L, "product10", 80.00));
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public Product getProductById(long id) {
        return products.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
