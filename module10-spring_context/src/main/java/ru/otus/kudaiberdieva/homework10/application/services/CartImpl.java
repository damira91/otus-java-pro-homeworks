package ru.otus.kudaiberdieva.homework10.application.services;

import ru.otus.kudaiberdieva.homework10.application.entity.Product;
import ru.otus.kudaiberdieva.homework10.application.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;


public class CartImpl implements Cart {

    private final List<Product> productsInCart = new ArrayList<>();

    private ProductRepository productRepository;

    @Override
    public void addProduct(Product product) {
        if (product != null) {
            productsInCart.add(product);
        }
    }

    @Override
    public void removeProduct(Product product) {
        productsInCart.remove(product);
    }

    @Override
    public List<Product> getProductsInCart() {
        return productsInCart;
    }
}
