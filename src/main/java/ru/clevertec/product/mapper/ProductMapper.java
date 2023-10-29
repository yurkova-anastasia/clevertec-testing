package ru.clevertec.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {

    /**
     * Маппит DTO в продукт без UUID
     *
     * @param productDto - DTO для маппинга
     * @return новый продукт
     */
    Product toProduct(ProductDto productDto);

    /**
     * Маппит текущий продукт в DTO без даты
     *
     * @param product - существующий продукт
     * @return DTO с идентификатором
     */
    InfoProductDto toInfoProductDto(Product product);

    /**
     * Сливает существующий продукт с информацией из DTO
     * не меняет дату создания и идентификатор
     *
     * @param product    существующий продукт
     * @param productDto информация для обновления
     * @return обновлённый продукт
     */
    Product merge(@MappingTarget Product product, ProductDto productDto);
}
