package ProductService.repo;

import ProductService.entity.LimitEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.time.Instant;

public interface LimitRepo extends CrudRepository<LimitEntity, Long> {

      /* Создание, уменьшения и контроля лимитов, реализовано в одном запросе с помощью табличных обобщений (CTE)
       * Если в сущности(таблице) Лимитов нет соответствующего пользователя(клиента) -> запрос создаст новый лимит (10 000.00),
       * за минусом текущего платежа.
       * Иначе возьмет текущий лимит по заданному пользователю(клиенту) и понизит на сумму платежа
       */
      @Query(value = "WITH new_values(idUser, sumlimit, dateinstall) "
                   + "   AS (values (:idUser, :sumPay, CAST(:revisionPay AS timestamp))), "
                   + "upsert AS (UPDATE limits l "
                   + "              SET sumlimit = (l.sumlimit - nv.sumlimit) "
                   + "              FROM new_values nv "
                   + "              WHERE l.idUser = nv.idUser "
                   + "              RETURNING l.id, l.idUser, l.sumlimit, l.dateinstall), "
                   + "insertt as (INSERT INTO limits(idUser, sumlimit, dateinstall) "
                   + "              SELECT idUser, 10000.00 - sumlimit, dateinstall"
                   + "              FROM new_values "
                   + "              WHERE NOT EXISTS (SELECT 1 FROM upsert) "
                   + "              RETURNING *) "
                   + "SELECT * FROM upsert"
                   + " UNION ALL "
                   + "SELECT * FROM insertt", nativeQuery = true)
      LimitEntity runLimit(Long idUser, BigDecimal sumPay, Instant revisionPay);


      @Query(value = "SELECT * FROM limits l "
                   + "WHERE l.idUser = :idUser", nativeQuery = true)
      LimitEntity findLimitForUser(Long idUser);

      @Query(value = "UPDATE limits "
                   + "SET dateinstall = CAST(:dateInstallLimit AS timestamp), "
                   + "    sumlimit = :sumLimit "
                   + "WHERE idUser = :idUser "
                   + "RETURNING *", nativeQuery = true)
      LimitEntity updateLimit(Long idUser, BigDecimal sumLimit, Instant dateInstallLimit);
}


