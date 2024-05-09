package exerciseCRUD;

import config.ConfigClass;
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

            userService.deleteAll();

            userService.createUser(NAME_USER);

            userService.createUser(LIST_USERS);

            userService.getUsersPredicate("User3");

            userService.getAllUsers();

            userService.deletePredicate("User2");

            userService.deleteAll();

        System.out.println("### End ###");
    }

}
