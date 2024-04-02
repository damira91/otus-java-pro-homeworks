package ru.otus.kudaiberdieva.homework2.classes;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.otus.kudaiberdieva.homework2.annotations.After;
import ru.otus.kudaiberdieva.homework2.annotations.Before;
import ru.otus.kudaiberdieva.homework2.annotations.Disabled;
import ru.otus.kudaiberdieva.homework2.annotations.Test;

@Disabled
public class TestClass2 {
    @BeforeSuite
    public void beforeSuite() {
        System.out.println("TestClass2 Before Suite");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("TestClass2 After Suite");
    }

    @Test(priority = 1)
    public void test08() {
        System.out.println("TestClass2 Test 8");
    }

    @Test(priority = 5)
    public void test09() {
        try {
            System.out.println("TestClass2 Test 9");
            throw new RuntimeException("TestClass2 Test 9 failed");
        } catch (Exception e) {
            System.err.println(" TestClass2 Test 9 failed: " + e.getMessage());
        }
    }

    @Test(priority = 9)
    public void test10() {
        System.out.println("TestClass2 Test 10");
    }

    @Disabled
    public void test11() {
        System.out.println("TestClass2 Test 11 is disabled");
    }

    @Before
    @Test(priority = 8)
    public void test12() {
        System.out.println("TestClass2 Test 12");
    }
    @After
    @Test(priority = 7)
    public void test13(){
        System.out.println("TestClass2 Test 13");
    }

    @After
    @Test(priority = 4)
    public void test14(){
        System.out.println("TestClass2 Test 14");
    }
}
