package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.util.ProductTestData;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.clevertec.product.util.ProductTestData.uuidExample;

class InMemoryProductRepositoryTest {

    private InMemoryProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository(new HashMap<>());
        Product product = ProductTestData.builder().build().buildProduct();
        Product product2 = ProductTestData.builder()
                .withUuid(UUID.fromString("61f0c404-5cb3-11e7-907b-a6006ad3dba1"))
                .withName("banana")
                .withCreated(LocalDateTime.of(2023, Month.OCTOBER, 28, 18, 0, 0))
                .build().buildProduct();
        productRepository.getProductMap().put(product.getUuid(), product);
        productRepository.getProductMap().put(product2.getUuid(), product2);
    }

    @Nested
    class FindById {

        @Test
        void findById_shouldReturnProductOptional_whenProductExist() {
            // given
            Product expected = ProductTestData.builder().build().buildProduct();

            // when
            Optional<Product> actual = productRepository.findById(uuidExample);

            assertAll(
                    () -> assertTrue(actual.isPresent()),
                    () -> assertEquals(expected, actual.get()));
        }

        @Test
        void findById_shouldReturnEmptyOptional_whenProductDoNotExist() {
            // when
            Optional<Product> actual = productRepository.findById(UUID.randomUUID());

            // then
            assertTrue(actual.isEmpty());
        }
    }

    @Test
    void findAll_shouldReturnListWithTwoProducts() {
        // given
        Product product1 = ProductTestData.builder().build().buildProduct();
        Product product2 = ProductTestData.builder()
                .withUuid(UUID.fromString("61f0c404-5cb3-11e7-907b-a6006ad3dba1"))
                .withName("banana")
                .withCreated(LocalDateTime.of(2023, Month.OCTOBER, 28, 18, 0, 0))
                .build().buildProduct();
        List<Product> expected = new ArrayList<>(List.of(product1, product2));

        // when
        List<Product> actual = productRepository.findAll();

        // then
        assertAll(
                () -> assertEquals(expected.size(), actual.size()),
                () -> assertEquals(expected, actual));
    }

    @Test
    void saveProduct_shouldReturnSavedProduct() {
        // given
        Product expected = ProductTestData.builder().build().buildProduct();
        UUID uuid = UUID.randomUUID();
        expected.setUuid(uuid);

        // when
        Product actual = productRepository.save(expected);

        // then
        assertEquals(actual, expected);
    }

    @Test
    void delete_shouldDeleteProduct_whenProductExist() {
        // when
        productRepository.delete(uuidExample);

        // then
        assertTrue(productRepository.findById(uuidExample).isEmpty());
    }
}