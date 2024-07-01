package ru.otus.kudaiberdieva.homework10.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class IOServiceImpl implements IOService {
    private static final Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void print(String message) {
        logger.info(message);
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
