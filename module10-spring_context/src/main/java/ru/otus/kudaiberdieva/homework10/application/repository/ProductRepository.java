package ru.otus.kudaiberdieva.homework10.application.repository;

import ru.otus.kudaiberdieva.homework10.application.entity.Product;

import java.util.List;

public interface ProductRepository {

    public List<Product> getProducts();

    Product getProductById(long id);
}
