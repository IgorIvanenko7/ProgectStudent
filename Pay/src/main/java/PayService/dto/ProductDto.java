package PayService.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    private Long id;
    private Long numberCount;
    private BigDecimal balans;
    private UserProductType typeProduct;
}
