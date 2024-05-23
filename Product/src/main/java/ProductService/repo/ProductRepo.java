package ProductService.repo;

import ProductService.entity.ProductEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepo extends CrudRepository<ProductEntity, Long> {

      @Query(value = "select * from products "
                   + "where id = :idProduct "
                   + "order by id", nativeQuery = true)
      List<ProductEntity> findProductId(Long idProduct);

      @Query(value = "update products "
                   + "set balans = balans - :divSum "
                   + "where iduser = :idUser and typeProduct = :typeProduct "
                   + "returning *", nativeQuery = true)
      List<ProductEntity> payProduct(Long idUser, String typeProduct, BigDecimal divSum);
}


