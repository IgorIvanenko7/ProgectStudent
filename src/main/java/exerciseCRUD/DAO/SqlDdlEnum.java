package exerciseCRUD.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SqlDdlEnum {

    insertUser("insert into users(username) values(:nameUser)", "Insert user"),

    selectUsers("select * from users " +
                         "where username :: text ilike :nameUser","Get Records"),

    deleteUsers("delete from users " +
                         "where username :: text ilike :nameUser", "Delete users");

    // --- Define Constructor Variables ---
   private final String querySQL;
   private final String note;
}

