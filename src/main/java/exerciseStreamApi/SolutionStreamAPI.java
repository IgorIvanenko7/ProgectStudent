package exerciseStreamApi;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static exerciseStreamApi.ListPosition.*;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;


public class SolutionStreamAPI {

    static List<Employee> employeeList = List.of(
            Employee.builder().name("Ivan").age(43).position(ENGINEER.name()) .build(),
            Employee.builder().name("Petr").age(47).position(ENGINEER.name()) .build(),
            Employee.builder().name("Irina").age(29).position(CSECRETARY.name()) .build(),
            Employee.builder().name("Nikolay").age(51).position(ENGINEER.name()) .build(),
            Employee.builder().name("Ekaterina").age(45).position(ACCOUNTANT.name()) .build(),
            Employee.builder().name("Sergey").age(50).position(ENGINEER.name()) .build(),
            Employee.builder().name("Alexandr").age(53).position(ENGINEER.name()) .build()
    );

    // Реализуйте удаление из листа всех дубликатов
    public static void getOne(){

        System.out.printf("Distinct List: %s%n",
                Stream.of(1, 2, 3, 3, 4, 5, 5, 5, 6, 7, 7, 7).distinct().toList());
        System.out.println("#----------------------------------------------#");
    }

    //  Найдите в списке целых чисел 3-е наибольшее число (пример: 5 2 10 9 4 3 10 1 13 => 10)
    public static void getTwo(){

        Integer findItem = Stream.of(5, 2, 10, 9, 4, 3, 10, 1, 13)
                .sorted(Comparator.comparing(x -> x, reverseOrder()))
                .skip(2)
                .findFirst()
                .orElseThrow();
        System.out.printf("Find item: %s%n", findItem);
        System.out.println("#----------------------------------------------#");
    }

    //  Найдите в списке целых чисел 3-е наибольшее «уникальное» число
    public static void getThree(){

        Integer findItem = Stream.of(5, 2, 10, 9, 4, 3, 10, 1, 13)
                .distinct()
                .sorted(Comparator.comparing(x -> x, reverseOrder()))
                .skip(2)
                .findFirst()
                .orElseThrow();
        System.out.printf("Find item: %s%n", findItem);
        System.out.println("#----------------------------------------------#");
    }

    //  Имеется список объектов типа Сотрудник (имя, возраст, должность), необходимо получить
    //  список имен 3 самых старших сотрудников с должностью «Инженер», в порядке убывания возраста
    public static void getFour(){

        List<String> resultListOld =  employeeList.stream()
                .filter(item -> item.getPosition().equals(ENGINEER.name()))
                .sorted(Comparator.comparing(Employee::getAge, reverseOrder()))
                .limit(3)
                .map(Employee ::getName)
                .toList();
        System.out.printf("Three every old: %s%n", resultListOld);
        System.out.println("#----------------------------------------------#");
    }

    // Имеется список объектов типа Сотрудник (имя, возраст, должность),
    // посчитайте средний возраст сотрудников с должностью «Инженер»
    public static void getFive(){

        double averageAge =  employeeList.stream()
                .filter(item -> item.getPosition().equals(ENGINEER.name()))
                .collect(Collectors.averagingInt(Employee::getAge));
        System.out.printf("Average Age of Engineer: %s%n", averageAge);
        System.out.println("#----------------------------------------------#");
    }

    // Найдите в списке слов самое длинное
    public static void getSix(){

        String longWord = Stream.of("PostgreSql", "Oracle", "SQL", "PL/SQL" ,"Java")
                .max(Comparator.comparing(String::length))
                .orElseThrow();
        System.out.printf("%s%n", longWord);
        System.out.println("#----------------------------------------------#");
    }

    // Имеется строка с набором слов в нижнем регистре, разделенных пробелом.
    // Постройте хеш-мапы, в которой будут хранится пары: слово - сколько раз оно встречается во входной строке
    public static void getSeven(){

        String strings = "postgresql oracle sql pl/sql postgresql java python postgresql java oracle";
        Map<String, Long> countStr = Arrays.stream(strings.split(" "))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.printf("Result count Str: %s%n", countStr);
        System.out.println("#----------------------------------------------#");
    }


    // Отпечатайте в консоль строки из списка в порядке увеличения длины слова,
    // если слова имеют одинаковую длины, то должен быть сохранен алфавитный порядок
    public static void getEight(){

        List<String> stringList = List.of("PostgreSql", "Oracle", "SQL", "PL/SQL" ,"Java", "AAA", "ABA", "ABB", "BBB");
        stringList.stream()
                  .sorted(Comparator.comparing(String :: length, naturalOrder())
                          .thenComparing(naturalOrder()))
                  .forEach(w -> System.out.printf("%s%n", w));
        System.out.println("#----------------------------------------------#");
    }

    // Имеется массив строк, в каждой из которых лежит набор из 5 строк, разделенных пробелом,
    // найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них
    public static void getNine(){

        String[] arrStr = {"Centos Sql Kafka Oracle MySql",
                          "Oracle Python Centos PostgreSql Ubuntu",
                          "SQL Mint PL/SQL AssemblerL Java"};

        String longWord = Arrays.stream(arrStr)
                .flatMap(str -> Arrays.stream(str.split(" ")))
                .max(Comparator.comparing(String::length))
                .orElseThrow();
        System.out.printf("%s%n", longWord);
        System.out.println("#----------------------------------------------#");
    }
}
