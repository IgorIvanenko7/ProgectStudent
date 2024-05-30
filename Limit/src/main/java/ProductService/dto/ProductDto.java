package ProductService.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    private Long id;
    private Long idUser;
    private Long numberCount;
    private BigDecimal balans;
    private UserProductType typeProduct;
}
