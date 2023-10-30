package ru.clevertec.product.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.util.ProductTestData;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperImplTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = Mappers.getMapper(ProductMapper.class);
    }

    @Test
    void toProduct_shouldMapProductDtoToProduct() {
        // given
        ProductDto productDto = ProductTestData.builder().build().buildProductDto();
        Product expected = ProductTestData.builder()
                .withUuid(null)
                .withCreated(null)
                .build().buildProduct();

        // when
        Product actual = productMapper.toProduct(productDto);

        // then
        assertEquals(expected, actual);

    }

    @Test
    void toInfoProductDto_shouldMapProductToInfoProductDto() {
        // given
        Product product = ProductTestData.builder().build().buildProduct();
        InfoProductDto expected = ProductTestData.builder().build().buildInfoProductDto();

        // when
        InfoProductDto actual = productMapper.toInfoProductDto(product);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void merge_shouldUpdateNameDescriptionAndPriceAndDoNotChangeUuidAndCreatedDate() {
        // given
        Product product = ProductTestData.builder().build().buildProduct();
        ProductDto productDto = ProductTestData.builder()
                .withName("newName")
                .withDescription("newDescription")
                .withPrice(BigDecimal.valueOf(2.2))
                .build().buildProductDto();
        Product expected = new Product(product.getUuid(), productDto.name(), productDto.description(),
                productDto.price(), product.getCreated());

        // when
        Product actual = productMapper.merge(product, productDto);

        // then
        assertEquals(expected, actual);
    }
}