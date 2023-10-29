package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.util.ProductTestData;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.clevertec.product.util.ProductTestData.uuidExample;

class InMemoryProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
    }

    @Nested
    class FindById {

        @Test
        void findById_shouldReturnProductOptional_whenProductExist() {
            // given
            Product expected = ProductTestData.builder().build().buildProduct();

            // when
            Optional<Product> actual = productRepository.findById(uuidExample);

            Assertions.assertTrue(actual.isPresent());
            Assertions.assertEquals(expected, actual.get());
        }

        @Test
        void findById_shouldReturnEmptyOptional_whenProductDoNotExist() {
            // when
            Optional<Product> actual = productRepository.findById(UUID.randomUUID());

            // then
            Assertions.assertTrue(actual.isEmpty());
        }
    }

    @Test
    void findAll_shouldReturnListWithTwoProducts() {
        // given
        Product product1 = ProductTestData.builder().build().buildProduct();
        Product product2 = ProductTestData.builder()
//                .withUuid(UUID.fromString("61f0c404-5cb3-11e7-907b-a6006ad3dba1"))
                .withName("banana")
                .withCreated(LocalDateTime.of(2023, Month.OCTOBER, 28, 18, 0, 0))
                .build().buildProduct();
        product2.setUuid(UUID.fromString("61f0c404-5cb3-11e7-907b-a6006ad3dba1"));
        List<Product> expected = new ArrayList<>(List.of(product1, product2));

        // when
        List<Product> actual = productRepository.findAll();

        // then
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void saveProduct_shouldReturnSavedProduct() {
        // given
        Product expected = ProductTestData.builder().build().buildProduct();

        // when
        Product actual = productRepository.save(expected);

        // then
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void delete_shouldDeleteProduct_whenProductExist() {
        // when
        productRepository.delete(uuidExample);

        // then
        Assertions.assertTrue(productRepository.findById(uuidExample).isEmpty());
    }
}