package exerciseCRUD;

import lombok.Data;
import org.springframework.stereotype.Repository;

@Data
@Repository
public class User {

    private Long id;
    private String username;
}
