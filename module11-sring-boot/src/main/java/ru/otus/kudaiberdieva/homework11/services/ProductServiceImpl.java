package ru.otus.kudaiberdieva.homework11.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.kudaiberdieva.homework11.dtos.CreateProductDTO;
import ru.otus.kudaiberdieva.homework11.entities.Product;
import ru.otus.kudaiberdieva.homework11.repositories.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.getProductById(productId);
    }

    @Override
    public Product createNewProduct(CreateProductDTO productDTO) {
        Product product = new Product(productDTO.getId(), productDTO.getTitle(), productDTO.getPrice());
        return productRepository.createProduct(product);

    }

    @Override
    public void delete(Long productId) {
        productRepository.delete(productId);
    }
}
