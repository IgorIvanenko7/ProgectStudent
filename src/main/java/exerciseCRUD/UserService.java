package exerciseCRUD;

import com.fasterxml.jackson.databind.ObjectMapper;
import exerciseCRUD.DAO.UserDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static exerciseCRUD.DAO.SqlDdlEnum.*;

@Service
public class UserService {

    private final UserDao userDao;
    private final ObjectMapper objectMapper;

    public UserService(UserDao userDao, ObjectMapper objectMapper) {
        this.userDao = userDao;
        this.objectMapper = objectMapper;
    }

    public void deleteAll() {
        deletePredicate("");
    }

    public void deletePredicate(String subNane) {
        userDao.queryDML(deleteUsers,
                Map.of("nameUser", "%" + subNane + "%"));
    }

    public void createUser(Object nameUser) {
        boolean resultInsert;
        if (nameUser instanceof List) {
            resultInsert = ((List<?>) nameUser).stream()
                    .map(user -> (String) user)
                    .map(rec -> userDao.queryDML(insertUser,
                            Map.of("nameUser", rec)))
                    .reduce(Boolean::logicalAnd)
                    .orElse(false);
        } else {
             resultInsert =  userDao.queryDML(insertUser,
                     Map.of("nameUser", (String) nameUser));
        }
        System.out.printf("### Status Insert record(s): %s ###%n ---------------------------%n", resultInsert);
    }

    public void getAllUsers () {
        getUsersPredicate("");
    }

    public void getUsersPredicate (String subNane) {
        System.out.println("### Output Recordset Users ###");
        var userDaoList = userDao.getRecords(User.class,
                Map.of ("nameUser", "%" + subNane + "%"));
        userDaoList.forEach(rec ->
                System.out.println(objectMapper.valueToTree(rec)));
        System.out.println("---------------------------------");
    }

}
