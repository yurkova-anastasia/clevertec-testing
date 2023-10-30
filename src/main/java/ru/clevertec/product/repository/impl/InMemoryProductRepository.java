package ru.clevertec.product.repository.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InMemoryProductRepository implements ProductRepository {

    private Map<UUID, Product> productMap;

    @Override
    public Optional<Product> findById(UUID uuid) {
        return Optional.ofNullable(productMap.get(uuid));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(productMap.values());
    }

    @Override
    public Product save(Product product) {
        UUID uuid = UUID.randomUUID();
        product.setUuid(uuid);
        if (product.getUuid() == null) {
            throw new IllegalArgumentException("UUID must be set before saving the product");
        }
        productMap.put(product.getUuid(), product);
        return product;
    }

    @Override
    public void delete(UUID uuid) {
        productMap.remove(uuid);
    }
}
