package ru.otus.kudaiberdieva.homework5;

import org.slf4j.LoggerFactory;
import ru.otus.kudaiberdieva.homework5.builder.Product;
import ru.otus.kudaiberdieva.homework5.iterator.Box;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        Product product = Product.builder()
                .id(1)
                .title("Television")
                .description("LCD screen")
                .cost(500.0)
                .weight(2.3)
                .width(5)
                .length(10)
                .height(15)
                .build();

        List<String> list1 = Arrays.asList("A1", "A2", "A3");
        List<String> list2 = Arrays.asList("B1", "B2");
        List<String> list3 = Arrays.asList("C1", "C2", "C3", "C4");
        List<String> list4 = Arrays.asList("D1");

        Box box = new Box(list1, list2, list3, list4);

        logger.info("Contents of the box:");
        for (String item : box) {
            logger.info(item);
        }
    }
}
