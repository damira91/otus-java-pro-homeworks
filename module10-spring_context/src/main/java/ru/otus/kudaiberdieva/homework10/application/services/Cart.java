package ru.otus.kudaiberdieva.homework10.application.services;

import ru.otus.kudaiberdieva.homework10.application.entity.Product;

import java.util.List;

public interface Cart {
    void addProduct(Product product);

    void removeProduct(Product product);

    List<Product> getProductsInCart();
}
