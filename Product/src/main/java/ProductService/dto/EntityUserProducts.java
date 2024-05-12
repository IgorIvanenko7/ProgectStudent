package ProductService.dto;

import exerciseCRUD.User;
import lombok.Data;

import java.util.List;

@Data
public class EntityUserProducts {
    private User user;
    private List<ProductDto> listProducts;
}
