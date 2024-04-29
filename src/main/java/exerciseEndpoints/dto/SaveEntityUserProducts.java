package exerciseEndpoints.dto;

import exerciseCRUD.User;
import lombok.Data;

import java.util.List;

@Data
public class SaveEntityUserProducts {
    private User user;
    private List<ProductDto> listProducts;
}
