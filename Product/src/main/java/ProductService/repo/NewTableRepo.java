package ProductService.repo;

import ProductService.entity.NewTableEntity;
import org.springframework.data.repository.CrudRepository;


public interface NewTableRepo extends CrudRepository<NewTableEntity, Long> {
}
