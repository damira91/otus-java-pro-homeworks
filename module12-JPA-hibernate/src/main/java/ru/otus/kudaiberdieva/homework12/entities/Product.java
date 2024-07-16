package ru.otus.kudaiberdieva.homework12.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Double price;

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)

    private Set<Customer> customers = new HashSet<>();

    public Product(String title, Double price, Set<Customer> customers) {
        this.title = title;
        this.price = price;
        if (customers != null) {
            this.customers.addAll(customers);
        }
    }

    public Product(String title, Double price) {
        this(title, price, new HashSet<>());
    }
}
