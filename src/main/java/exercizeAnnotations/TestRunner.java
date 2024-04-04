package exercizeAnnotations;

// Marker annotations & run tests

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Comparator.reverseOrder;

public class TestRunner {

    static int tests = 0;
    static int passed = 0;

    static List<Method> listBeforeSuite = new ArrayList<>();
    static List<Method> listAfterSuite = new ArrayList<>();

    public static void runTests(Class<?> testClass) throws InstantiationException,
                                                           IllegalAccessException {

        //Create instance class
        var newInstance = testClass.newInstance();

        // Creating a new Stream every foreach -> Supplier
        Supplier<Stream<Method>> streamSupplier = () -> Arrays.stream(testClass.getDeclaredMethods()) ;

        // За один проход создаем два листа методов с @BeforeSuite &  @AfterSuite
        streamSupplier.get()
                .forEach(m -> {
                    if (m.isAnnotationPresent(BeforeSuite.class)) {
                        listBeforeSuite.add(m);
                    } else if (m.isAnnotationPresent(AfterSuite.class))
                        listAfterSuite.add(m);
                });

        // Validation count annotation @BeforeSuite & Run
        if(listBeforeSuite.size() > 1){
            throw new IllegalStateException("Methode with annotation @BeforeSuite more One");
        } else {
            try {
                boolean resultTest = (boolean) listBeforeSuite.get(0).invoke(newInstance);
                System.out.printf("Run methode: %s, Result: %s%n", listBeforeSuite.get(0).getName(), resultTest);
                System.out.println("#----------------------------------------------#");
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        // Detect methode with annotation @BeforeTest
        Method beforeEveryTest = streamSupplier.get()
                .filter(m -> m.isAnnotationPresent(BeforeTest.class))
                .findFirst()
                .orElse(null);

        // Detect methode with annotation @AfterTest
        Method afterEveryTest = streamSupplier.get()
                .filter(m -> m.isAnnotationPresent(AfterTest.class))
                .findFirst()
                .orElse(null);

        // Call methods with @Test annotation in revers(DESCending) order priority
        streamSupplier.get()
                .filter(m -> m.isAnnotationPresent(Test.class))
                .sorted(Comparator.comparing(s -> s.getAnnotation(Test.class)
                        .priority(), reverseOrder()))
                .forEach(m -> {
                    tests ++;
                    try {
                        // Call @BeforeTest if exists
                        if (beforeEveryTest != null) {
                            beforeEveryTest.invoke(newInstance);
                        }
                        var resultTest = m.invoke(newInstance);
                        System.out.printf("Run methode: %s With priority - %s, Result: %s%n", m.getName(),
                                m.getAnnotation(Test.class).priority() , resultTest);

                        // Call @AfterTest if exists
                        if (afterEveryTest != null) {
                            afterEveryTest.invoke(newInstance);
                        }
                        System.out.println("#----------------------------------------------#");
                        passed ++;
                    } catch (InvocationTargetException wrappedExc) {
                        Throwable exc = wrappedExc.getCause();
                        System.out.println(m + " failed: " + exc);
                    } catch (IllegalAccessException exc) {
                        System.out.println("Invalid @Test: " + m);
                    }
                });

        System.out.printf("Result methods with annotations @Test -> Passed: %d, Failed: %d%n",
                passed, tests - passed);
        System.out.println("#----------------------------------------------#");

        // Call method with Annotation @CsvSource (if exists)
        streamSupplier.get()
            .filter(m -> m.isAnnotationPresent(CsvSource.class))
            .forEach(m -> {
                String parsStr = m.getAnnotation(CsvSource.class).parsStr();
                List<String> l = Arrays.asList(parsStr.split(","));
                try {
                    boolean resultCs = (boolean) m.invoke(newInstance, Integer.parseInt(l.get(0).trim()), l.get(1),
                            Integer.parseInt(l.get(2).trim()), Boolean.valueOf(l.get(3)));
                    System.out.printf("# Result run method with annotation @CsvSource : %s%n", resultCs);
                    System.out.println("#----------------------------------------------#");
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });

        // Validation count annotation  @AfterSuite & Run
        if(listAfterSuite.size() > 1){
            throw new IllegalStateException("Methode with annotation @AfterSuite more One");
        } else {
            try {
                boolean resultTest = (boolean) listAfterSuite.get(0).invoke(newInstance);
                System.out.printf("Run methode: %s, Result: %s%n", listAfterSuite.get(0).getName(), resultTest);
                System.out.println("#----------------------------------------------#");
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("SOURCE");
        System.out.println("COMMENT");
        System.out.println("COMMENT1");

    }
}