package ru.otus.kudaiberdieva.homework2.classes;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.otus.kudaiberdieva.homework2.annotations.After;
import ru.otus.kudaiberdieva.homework2.annotations.Before;
import ru.otus.kudaiberdieva.homework2.annotations.Disabled;
import ru.otus.kudaiberdieva.homework2.annotations.Test;

public class TestClass1 {
    @BeforeSuite
    public void beforeSuite() {
        System.out.println("TestClass1 Before Suite");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("TestClass1 After Suite");
    }

    @Test(priority = 1)
    public void test01() {
        System.out.println("TestClass1 Test 1");
    }

    @Test(priority = 5)
    public void test02() {
        try {
            System.out.println("TestClass1 Test 2");
            throw new RuntimeException("TestClass1 Test 2 failed");
        } catch (Exception e) {
            System.err.println(" TestClass1 Test 2 failed: " + e.getMessage());
        }
    }

    @Test(priority = 10)
    public void test03() {
        System.out.println("TestClass1 Test 3");
    }

    @Disabled
    public void test04() {
        System.out.println("TestClass1 Test 4 is disabled");
    }

    @Before
    @Test(priority = 9)
    public void test05() {
        System.out.println("TestClass1 Test 5");
    }
    @After
    @Test(priority = 7)
    public void test06(){
        System.out.println("TestClass1 Test 6");
    }

    @After
    @Test(priority = 4)
    public void test07(){
        System.out.println("TestClass1 Test 7");
    }
}
