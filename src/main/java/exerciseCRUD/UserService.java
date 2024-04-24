package exerciseCRUD;

import com.fasterxml.jackson.databind.ObjectMapper;
import exerciseCRUD.DAO.UserDao;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

import static exerciseCRUD.DAO.SqlDdlEnum.*;

@Service
public class UserService {

    private final UserDao userDao;
    private final ObjectMapper objectMapper;

    public UserService(UserDao userDao, ObjectMapper objectMapper) {
        this.userDao = userDao;
        this.objectMapper = objectMapper;
    }
    //----------------------------------

    // Удаление всех записей
    public void deleteAll() {
        deletePredicate("");
    }

    // Универсальный delete -> c предикатом
    public void deletePredicate(String subNane) {
        userDao.queryDML(deleteWithPredicate,
                new HashMap<>() {{put("nameUser", "%" + subNane + "%");}});
    }

    // Принимает(добавляет) сисок пользователей или одного пользователя
    public void createUser(Object nameUser) {
        boolean resultInsert;
        if (nameUser instanceof List) {
            resultInsert = ((List<?>) nameUser).stream()
                    .map(user -> (String) user)
                    .map(rec -> userDao.queryDML(insertRecord,
                                new HashMap<>() {{put("nameUser", rec);}}))
                    .reduce(Boolean::logicalAnd)
                    .orElse(false);
        } else {
             resultInsert =  userDao.queryDML(insertRecord,
                            new HashMap<>() {{put("nameUser", (String) nameUser);}});
        }
        System.out.printf("### Status Insert record(s): %s ###%n ---------------------------%n", resultInsert);
    }

    // Получение всех записей
    public void getAllUsers () {
        getUsersPredicate("");
    }

    // Универсальный Select -> c предикатом
    public void getUsersPredicate (String subNane) {
        System.out.println("### Output Recordset Users ###");
        var userDaoList = userDao.getRecords(User.class,
                new HashMap<>() {{put("nameUser", "%" + subNane + "%");}});
        userDaoList.forEach(rec ->
                System.out.println(objectMapper.valueToTree(rec)));
        System.out.println("---------------------------------");
    }
}
