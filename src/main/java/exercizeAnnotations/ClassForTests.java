package exercizeAnnotations;

public class ClassForTests {

    @BeforeTest
    public boolean methodBeforeTest(){
        System.out.println("### Run Method before Test ###");
        return true;
    }

    @AfterTest
    public boolean methodAfterTest(){
        System.out.println("### Run Method after Test ###");
        return true;
    }
    //---------------------------------------------------
    @Test
    public boolean methodTest1(){
        System.out.println("### Run Method Test1 with default \"priority\" ###");
        return true;
    }

    @Test (priority = 1)
    public boolean methodTest2(){
        System.out.println("### Run Method Test2 with \"priority\" equals - 1 ###");
        return true;
    }

    @Test (priority = 3)
    public boolean methodTest3(){
        System.out.println("### Run Method Test2 with \"priority\" equal - 3 ###");
        return true;
    }
    //---------------------------------------------------
    @BeforeSuite
    public boolean methodBefore(){
        System.out.println("### Run Method Before ###");
        return true;
    }

    @AfterSuite
    public boolean methodAfter(){
        System.out.println("### Run Method After ###");
        return true;
    }
    //---------------------------------------------------
    @CsvSource (parsStr = "5, Java, 7, true")
    public boolean methodParsCsv(int var0, String str1, int var1, boolean vb){

        for (int i = 0; i < var0; i++){
            System.out.printf("# First STR: %s | Line № %s%n", str1, i);
        }
        for (int i = 0; i < var1; i++){
            System.out.printf("# Second BOOLEAN: %s | Line № %s%n", vb, i);
        }
        return true;
    }

}
