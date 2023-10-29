package ru.clevertec.product.util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class ProductTestData {

    public static final UUID uuidExample = UUID.fromString("61f0c404-5cb3-11e7-907b-a6006ad3dba0");

    @Builder.Default
    public UUID uuid = uuidExample;

    @Builder.Default
    public String name = "apple";

    @Builder.Default
    public String description = "green";

    @Builder.Default
    public BigDecimal price = BigDecimal.valueOf(1.1);

    @Builder.Default
    public LocalDateTime created =
            LocalDateTime.of(2023, Month.OCTOBER, 28, 17, 0, 0);

    public Product buildProduct() {
        return new Product(uuidExample, name, description, price, created);
    }

    public ProductDto buildProductDto() {
        return new ProductDto(name, description, price);
    }

    public InfoProductDto buildInfoProductDto() {
        return new InfoProductDto(uuidExample, name, description, price);
    }
}
