package ru.otus.kudaiberdieva.homework2.testRunner;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.otus.kudaiberdieva.homework2.annotations.After;
import ru.otus.kudaiberdieva.homework2.annotations.Before;
import ru.otus.kudaiberdieva.homework2.annotations.Disabled;
import ru.otus.kudaiberdieva.homework2.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;


public class MyTestFramework {
    private Method beforeSuiteMethod;
    private Method afterSuiteMethod;
    private List<Method> before;
    private List<Method> after;
    private List<Method> methods;
    private boolean skippedClass;
    private boolean testFailed;

    private int successfulTests = 0;
    private int failedTests = 0;
    private int totalTests = 0;
    private int skippedTest = 0;

    private Class<?> classRun;

    public MyTestFramework(Class<?> clazz) {
        this.classRun = clazz;
        before = new ArrayList<>();
        after = new ArrayList<>();
        methods = new ArrayList<>();
        initTest();
    }

    private void getMethodAnnotations(Method method) {
        if (method.isAnnotationPresent(Disabled.class)) {
            skippedTest++;
            return;
        }

        if (method.isAnnotationPresent(BeforeSuite.class) && !method.isAnnotationPresent(Test.class)) {
            if (beforeSuiteMethod != null) {
                testFailed = true;
                System.out.println("BeforeSuite is used more than once");
                return;
            }
            beforeSuiteMethod = method;
            return;
        }

        if (method.isAnnotationPresent(AfterSuite.class) && !method.isAnnotationPresent(Test.class)) {
            if (afterSuiteMethod != null) {
                testFailed = true;
                System.out.println("AfterSuite is used more than once");
                return;
            }
            afterSuiteMethod = method;
            return;
        }

        if ((method.isAnnotationPresent(Before.class) || method.isAnnotationPresent(After.class))
                && method.isAnnotationPresent(Test.class)) {
            Test test = method.getAnnotation(Test.class);
            if (!(test.priority() >= 1 && test.priority() <= 10)) {
                return;
            }

            if (method.isAnnotationPresent(Before.class)) {
                before.add(method);
            } else {
                after.add(method);
            }
            return;
        }

        if (method.isAnnotationPresent(Test.class)) {
            methods.add(method);
        }
    }

    private void initTest() {
        if (classRun.isAnnotationPresent(Disabled.class)) {
            skippedClass = true;
            return;
        }

        for (Method method : classRun.getDeclaredMethods()) {
            totalTests++;
            getMethodAnnotations(method);
        }

        Comparator<Method> methodComparator = (m1, m2) -> {
            Test tm1 = m1.getAnnotation(Test.class);
            Test tm2 = m2.getAnnotation(Test.class);
            int priorm1 = (tm1 != null) ? tm1.priority() : 0;
            int priorm2 = (tm2 != null) ? tm2.priority() : 0;
            return Integer.compare(priorm2, priorm1);
        };
    }

    public void runTest() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (skippedClass) {
            System.out.println("Disable annotation is used in class - no test was run");
            return;
        }

        if (testFailed) {
            System.out.println("Error - no test was run");
            return;
        }

        Object o = classRun.getConstructor().newInstance();

        System.out.println("@BeforeSuite");
        runMethod(beforeSuiteMethod, o);

        for (Method methodRun : methods) {
            System.out.println();
            for (Method methodBefore : before) {
                System.out.println("@Before");
                runMethod(methodBefore, o);
            }

            System.out.println("@Test" + " priority=" + methodRun.getAnnotation(Test.class).priority());
            runMethod(methodRun, o);

            for (Method methodAfter : after) {
                System.out.println("@After");
                runMethod(methodAfter, o);
            }
        }

        System.out.println("\n @AfterSuite");
        runMethod(afterSuiteMethod, o);

        System.out.println("Testing statistics:");
        System.out.println("Total tests: " + totalTests);
        System.out.println("Skipped (Disabled): " + skippedTest);
        System.out.println("Succeeded: " + successfulTests);
        System.out.println("Failed: " + failedTests);
    }

    private void runMethod(Method m, Object o) {
        if (m == null) {
            return;
        }
        try {
            m.invoke(o);
            successfulTests++;
        } catch (Exception e) {
            System.out.println("!!!Error - " + e);
            failedTests++;
        }
    }
}
