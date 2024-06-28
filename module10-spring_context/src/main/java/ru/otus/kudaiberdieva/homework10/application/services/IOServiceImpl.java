package ru.otus.kudaiberdieva.homework10.application.services;

import java.util.Scanner;

public class IOServiceImpl implements IOService {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        return scanner.nextLine();
    }

    @Override
    public Long readLong() {
        return Long.parseLong(scanner.nextLine());
    }
}
