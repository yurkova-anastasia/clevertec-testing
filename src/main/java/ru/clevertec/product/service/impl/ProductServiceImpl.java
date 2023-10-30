package ru.clevertec.product.service.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository productRepository;

    @Override
    public InfoProductDto getById(UUID uuid) {
        Product product = productRepository.findById(uuid).orElseThrow(() ->
                new ProductNotFoundException(uuid));
        return mapper.toInfoProductDto(product);
    }

    @Override
    public List<InfoProductDto> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(mapper::toInfoProductDto)
                .toList();
    }

    @Override
    public UUID create(ProductDto productDto) {
        if (productDto == null || productDto.name() == null
                || productDto.description() == null || productDto.price() == null
                || productDto.price().compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("ProductDto or its fields cannot be null");
        }
        Product product = mapper.toProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return savedProduct.getUuid();
    }

    @Override
    public void update(UUID uuid, ProductDto productDto) {
        Product product = productRepository.findById(uuid).orElseThrow(() ->
                new ProductNotFoundException(uuid));
        Product updatedProduct = mapper.merge(product, productDto);
        productRepository.save(updatedProduct);
    }

    @Override
    public void delete(UUID uuid) {
        productRepository.delete(uuid);
    }
}
