package ru.otus.kudaiberdieva.homework2.testRunner;


import ru.otus.kudaiberdieva.homework2.classes.TestClass1;
import ru.otus.kudaiberdieva.homework2.classes.TestClass2;

public class Main {
    public static void main(String[] args) {
        MyTestFramework testFramework = new MyTestFramework(TestClass1.class);
        try {
            testFramework.runTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyTestFramework testFramework2 = new MyTestFramework(TestClass2.class);
        try {
            testFramework2.runTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

