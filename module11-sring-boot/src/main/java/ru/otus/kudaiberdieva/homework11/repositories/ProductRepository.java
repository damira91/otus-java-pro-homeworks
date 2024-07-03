package ru.otus.kudaiberdieva.homework11.repositories;

import ru.otus.kudaiberdieva.homework11.entities.Product;

import java.util.List;


public interface ProductRepository {
    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product createProduct(Product product);

    void delete(Long id);
}