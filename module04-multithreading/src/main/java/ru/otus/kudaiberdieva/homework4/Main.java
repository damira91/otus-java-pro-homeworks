package ru.otus.kudaiberdieva.homework4;

public class Main {
    public static void main(String[] args) {

        ThreadPool threadPool = new ThreadPool(5);

        for (int i = 0; i < 50; i++) {
            int taskNumber = i + 1;
            threadPool.execute(() -> {
                System.out.println("Task " + taskNumber + " executed by " + Thread.currentThread().getName());
            });
        }
        threadPool.shutdown();
    }
}
