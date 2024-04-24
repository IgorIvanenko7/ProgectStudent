package exerciseCRUD;

import exerciseCRUD.DAO.ConfigClass;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.List;


public class Start {

    private final static String  NAME_USER = "UserDB";
    private final static List<String> LIST_USERS = List.of("User1", "User2", "User3", "UserForDel3", "User4", "User5", "User6", "User7");


    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ConfigClass.class);
        UserService userService = context.getBean(UserService.class);
        //----------------------------------------------------------------
        System.out.println("### Begin ###");

            // Добавление пользователя -> скалярное значение (одна запись)
            userService.createUser(NAME_USER);

            // Добавление списка пользователей (> 1 записей)
            userService.createUser(LIST_USERS);

            // Получить заданного пользователя(лей) по имени (любому вхождению)
            userService.getUsersPredicate("User3");

            // Получить всех пользователей (RecordSet)
            userService.getAllUsers();

            // Удаление заданного пользователя -> предикат по имени(любому вхождению)
            userService.deletePredicate("User2");

            //Удаление всех записей
            userService.deleteAll();

        System.out.println("### End ###");
    }

}
