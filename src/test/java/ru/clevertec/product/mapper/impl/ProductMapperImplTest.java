package ru.clevertec.product.mapper.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.mapper.ProductMapperImpl;
import ru.clevertec.product.util.ProductTestData;

import java.math.BigDecimal;

class ProductMapperImplTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapperImpl();
    }

    @Test
    void toProduct_shouldMapProductDtoToProduct() {
        // given
        ProductDto productDto = ProductTestData.builder().build().buildProductDto();
        Product expected = ProductTestData.builder()
                .withCreated(null)
                .build().buildProduct();
        expected.setUuid(null);

        // when
        Product actual = productMapper.toProduct(productDto);

        // then
        Assertions.assertEquals(expected, actual);

    }

    @Test
    void toInfoProductDto_shouldMapProductToInfoProductDto() {
        // given
        Product product = ProductTestData.builder().build().buildProduct();
        InfoProductDto expected = ProductTestData.builder().build().buildInfoProductDto();

        // when
        InfoProductDto actual = productMapper.toInfoProductDto(product);

        // then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void merge_shouldUpdateNameDescriptionAndPriceAndDoNotChangeUuidAndCreatedDate() {
        // given
        Product product = ProductTestData.builder().build().buildProduct();
        ProductDto productDto = new ProductDto("newName", "newDescription", BigDecimal.valueOf(2.2));
        Product expected = new Product(product.getUuid(), productDto.name(), productDto.description(),
                productDto.price(), product.getCreated());

        // when
        Product actual = productMapper.merge(product, productDto);

        // then
        Assertions.assertEquals(expected, actual);
    }
}