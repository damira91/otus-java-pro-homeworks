package ru.otus.kudaiberdieva.homework11.repositories;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.otus.kudaiberdieva.homework11.entities.Product;
import ru.otus.kudaiberdieva.homework11.exceptions.EntityException;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductInMemoryRepository implements ProductRepository {
    private List<Product> products;

    @PostConstruct
    public void init(){
        this.products = new ArrayList<>();
        this.products.add(new Product(1L, "product1", 10.00));
        this.products.add(new Product(2L, "product2", 15.00));
        this.products.add(new Product(3L, "product3", 25.00));
        this.products.add(new Product(4L, "product4", 45.00));
        this.products.add(new Product(5L, "product5", 60.00));
    }
   @Override
    public List<Product> getAllProducts(){
    return products;
   }
   @Override
    public Product getProductById(Long id){
       return products.stream().filter(p -> p.getId().equals(id)).findFirst()
               .orElseThrow(() -> new EntityException(String.format("There is no product with id %s", id)));
   }
   @Override
    public Product createProduct(Product product){
        products.add(product);
        return product;
   }
    @Override
    public void delete(Long id) {
        products.removeIf(it -> it.getId().equals(id));
    }
}
