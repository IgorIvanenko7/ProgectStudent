package ProductService.repo;

import ProductService.entity.ProductEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepo extends CrudRepository<ProductEntity, Long> {

      @Query(value = "select * from products " +
                     "where id = :idProduct " +
                     "order by id", nativeQuery = true)
      List<ProductEntity> findProductId(Long idProduct);
}


