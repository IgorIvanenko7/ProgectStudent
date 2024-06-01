package ProductService.repo;

import ProductService.entity.LimitEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.time.Instant;

public interface LimitRepo extends CrudRepository<LimitEntity, Long> {


      @Query(value = "WITH new_values(idUser, sumlimit, dateinstall) "
                   + "   AS (values (:idUser, :sumPay, current_timestamp)), "
                   + "upsert AS (UPDATE limits l "
                   + "              SET sumlimit = (l.sumlimit - nv.sumlimit) "
                   + "              FROM new_values nv "
                   + "              WHERE l.idUser = nv.idUser "
                   + "              RETURNING l.id, l.idUser, l.sumlimit, l.dateinstall), "
                   + "insertt as (INSERT INTO limits(idUser, sumlimit, dateinstall) "
                   + "              SELECT idUser, :sumPay, dateinstall"
                   + "              FROM new_values "
                   + "              WHERE NOT EXISTS (SELECT 1 FROM upsert) "
                   + "              RETURNING *) "
                   + "SELECT * FROM upsert"
                   + " union all "
                   + "SELECT * FROM insertt", nativeQuery = true)
      LimitEntity runLimit(Long idUser, BigDecimal sumPay /*, Instant revisionPay*/);
}


