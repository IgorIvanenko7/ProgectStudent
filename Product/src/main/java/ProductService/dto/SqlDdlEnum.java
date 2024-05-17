package ProductService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SqlDdlEnum {

    insertUser("insert into users(username) values(:nameUser)", "Insert user"),

    selectUsers("select * from users " +
                         "where username :: text ilike :nameUser","Get Records"),

    deleteUsers("delete from users " +
                         "where username :: text ilike :nameUser", "Delete users"),

    selectProduct("select * from products " +
                           "where id :: int = :idProduct", "Get products"),

    selectProducts("select * from products", "Get products"),

    selectProductsForUserId("select * from products p " +
                                     "join users u " +
                                     "using(id) " +
                                     "where u.id :: int = :idUser", "Get products"),

    insertProductForCurrentUser("with getUser as (select id from users " +
                                         "where username = :nameUser) " +
                                         "insert into products values ((select id from getUser), :numberCount :: bigint , :balans :: numeric, :typeProduct :: user_product_type )",
            "Save entity"),

    payProduct("update products " +
                        "set balans = balans - (:divSum :: bigint) " +
                        "where id :: int = :idUser and typeProduct :: text = :typeProduct ", "Pay product");

    // --- Define Constructor Variables ---
   private final String querySQL;
   private final String note;
}