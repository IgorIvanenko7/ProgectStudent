package ProductService.repo;

import ProductService.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface UserRepo extends CrudRepository<UserEntity, Long> {

      @Query("SELECT u FROM UserEntity u WHERE u.id = :idUser")
      UserEntity findUserId(Long idUser);
}
