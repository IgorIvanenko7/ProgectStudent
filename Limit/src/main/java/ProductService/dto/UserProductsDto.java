package ProductService.dto;


import lombok.Data;

import java.util.List;

@Data
public class UserProductsDto {
    private UserDto user;
    private List<ProductDto> listProducts;
}
