package exercizeAnnotations;

public class Start {
    public static void main(String[] args) throws InstantiationException,
                                                  IllegalAccessException {
        TestRunner.runTests(ClassForTests.class);
        System.out.println("# End tests #");
    }
}
