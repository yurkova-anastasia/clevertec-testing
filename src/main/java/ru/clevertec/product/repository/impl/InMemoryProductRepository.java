package ru.clevertec.product.repository.impl;

import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryProductRepository implements ProductRepository {

    private final Product product = new Product(UUID.fromString("61f0c404-5cb3-11e7-907b-a6006ad3dba0"),
            "apple", "green", BigDecimal.valueOf(1.1),
            LocalDateTime.of(2023, Month.OCTOBER, 28, 17, 0, 0));

    private final Product product2 = new Product(UUID.fromString("61f0c404-5cb3-11e7-907b-a6006ad3dba1"),
            "banana", "green", BigDecimal.valueOf(1.1),
            LocalDateTime.of(2023, Month.OCTOBER, 28, 18, 0, 0));

    public List<Product> products = new ArrayList<>(List.of(product, product2));

    @Override
    public Optional<Product> findById(UUID uuid) {
        return products.stream()
                .filter(p -> p.getUuid().equals(uuid))
                .findFirst();
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Product save(Product product) {
        products.add(product);
        return product;
    }

    @Override
    public void delete(UUID uuid) {
        products.removeIf(p -> p.getUuid().equals(uuid));
    }
}
