package ru.otus.kudaiberdieva.homework11.services;

import ru.otus.kudaiberdieva.homework11.dtos.CreateProductDTO;
import ru.otus.kudaiberdieva.homework11.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product createNewProduct(CreateProductDTO productDTO);

    void delete(Long productId);
}
