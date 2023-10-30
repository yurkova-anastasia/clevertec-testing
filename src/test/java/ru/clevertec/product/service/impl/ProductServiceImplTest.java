package ru.clevertec.product.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.impl.InMemoryProductRepository;
import ru.clevertec.product.util.ProductTestData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.product.util.ProductTestData.uuidExample;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private InMemoryProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @BeforeEach
    void setup() {
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
    class GetById {

        @Test
        void getById_shouldReturnInfoProductDto_whenProductExist() {
            // given
            Product product = ProductTestData.builder().build().buildProduct();
            Optional<Product> optionalProduct = Optional.of(product);
            InfoProductDto expected = ProductTestData.builder().build().buildInfoProductDto();

            when(productRepository.findById(uuidExample)).thenReturn(optionalProduct);
            when(productMapper.toInfoProductDto(product)).thenReturn(expected);

            // when
            InfoProductDto actual = productService.getById(uuidExample);

            //then
            assertEquals(expected, actual);
        }

        @Test
        void getById_shouldThrowProductNotFoundException_whenProductDoNotExist() {
            // given
            UUID uuid = UUID.randomUUID();

            when(productRepository.findById(uuid)).thenReturn(Optional.empty());

            //then
            assertThrows(ProductNotFoundException.class, () -> productService.getById(uuid));
        }
    }

    @Test
    void getAll_shouldReturnListOfInfoProductDto() {
        // given
        Product product1 = ProductTestData.builder().build().buildProduct();
        Product product2 = ProductTestData.builder()
                .withName("banana")
                .build().buildProduct();
        List<Product> products = List.of(product1, product2);

        InfoProductDto infoProductDto1 = ProductTestData.builder().build().buildInfoProductDto();
        InfoProductDto infoProductDto2 = ProductTestData.builder()
                .withName("banana")
                .build().buildInfoProductDto();
        List<InfoProductDto> expected = List.of(infoProductDto1, infoProductDto2);

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toInfoProductDto(product1)).thenReturn(infoProductDto1);
        when(productMapper.toInfoProductDto(product2)).thenReturn(infoProductDto2);

        // when
        List<InfoProductDto> actual = productService.getAll();

        // then
        assertAll(
                () -> assertEquals(expected.size(), actual.size()),
                () -> assertEquals(expected, actual));
    }

    @Nested
    class Create {

        @Test
        void create_shouldCreateProductAndReturnUUID() {
            // given
            Product productToSave = ProductTestData.builder()
                    .build().buildProduct();
            ProductDto productDto = ProductTestData.builder()
                    .build().buildProductDto();

            when(productMapper.toProduct(productDto)).thenReturn(productToSave);
            when(productRepository.save(productToSave)).thenReturn(productToSave);

            // when
            productService.create(productDto);

            // then
            verify(productRepository).save(productCaptor.capture());
            assertThat(productCaptor.getValue())
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, uuidExample);
        }

        @ParameterizedTest
        @MethodSource("invalidProductDtoProvider")
        void create_shouldThrowException_whenInvalidProduct(ProductDto invalidProductDto) {
            // when/then
            assertThrows(IllegalArgumentException.class, () -> productService.create(invalidProductDto));
        }

        private static Stream<ProductDto> invalidProductDtoProvider() {
            return Stream.of(
                    new ProductDto(null, "Sample Description", BigDecimal.valueOf(10.0)),
                    new ProductDto("Sample Product", null, BigDecimal.valueOf(10.0)),
                    new ProductDto("Sample Product", "Sample Description", null),
                    new ProductDto("Sample Product", "Sample Description", BigDecimal.valueOf(-0.2))
            );
        }
    }

    @Nested
    class Update {

        @Test
        void update_shouldUpdateProduct_whenProductExist() {
            // given
            Product product = ProductTestData.builder().build().buildProduct();
            ProductDto productDto = ProductTestData.builder()
                    .withName("Updated Product")
                    .withDescription("Updated Description")
                    .withPrice(BigDecimal.valueOf(15.0))
                    .build().buildProductDto();
            Product updatedProduct = new Product(uuidExample, productDto.name(),
                    productDto.description(), productDto.price());

            when(productMapper.merge(product, productDto))
                    .thenReturn(updatedProduct);
            when(productRepository.save(Mockito.any(Product.class)))
                    .thenReturn(updatedProduct);
            when(productRepository.findById(uuidExample)).thenReturn(Optional.of(product));

            // when

            productService.update(uuidExample, productDto);

            // then
            verify(productRepository).save(productCaptor.capture());

            assertAll(
                    () -> assertNotNull(productCaptor.getValue()),
                    () -> assertEquals(updatedProduct, productCaptor.getValue()));
        }

        @Test
        void update_shouldThrowException_whenProductDoNotExist() {
            // given
            UUID nonExistentUUID = UUID.randomUUID();
            ProductDto updatedProductDto = ProductTestData.builder()
                    .withName("Updated Product")
                    .withDescription("Updated Description")
                    .withPrice(BigDecimal.valueOf(15.0))
                    .build().buildProductDto();

            when(productRepository.findById(nonExistentUUID)).thenReturn(Optional.empty());

            // then
            assertThrows(ProductNotFoundException.class, () -> productService.update(nonExistentUUID, updatedProductDto));
        }
    }

    @Test
    void delete_shouldDeleteProduct_whenProductExist() {
        // when
        productService.delete(uuidExample);

        // then
        verify(productRepository, times(1)).delete(uuidExample);
    }
}
