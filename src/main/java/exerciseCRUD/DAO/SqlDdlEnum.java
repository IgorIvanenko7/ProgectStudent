package exerciseCRUD.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SqlDdlEnum {

    insertRecord ("insert into users(username) values(:nameUser)", "Insert record"),
    selectRecords("select * from users " +
                           "where username :: text ilike :nameUser","Get Records"),
    deleteAll("delete from users ", "Simple delete"),
    deleteWithPredicate("delete from users " +
                                 "where username :: text ilike :nameUser", "Delete with Predicate");

    // --- Define Constructor Variables ---
   private final String querySQL;
   private final String note;
}

