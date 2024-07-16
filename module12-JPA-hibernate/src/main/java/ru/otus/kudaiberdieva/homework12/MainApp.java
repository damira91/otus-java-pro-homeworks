package ru.otus.kudaiberdieva.homework12;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.kudaiberdieva.homework12.configurations.SessionFactoryConfiguration;
import ru.otus.kudaiberdieva.homework12.entities.Customer;
import ru.otus.kudaiberdieva.homework12.entities.Product;
import ru.otus.kudaiberdieva.homework12.utils.EntityUtil;


public class MainApp {
    private static SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {

        sessionFactory = SessionFactoryConfiguration.init();

        logger.info("All customers:");
        EntityUtil.findAll(sessionFactory, Customer.class).forEach(customer -> logger.info(customer.toString()));
        logger.info("All products:");
        EntityUtil.findAll(sessionFactory, Product.class).forEach(product -> logger.info(product.toString()));

        logger.info("Customer with id", EntityUtil.findById(sessionFactory, Customer.class, 2L));
        logger.info("Product with id", EntityUtil.findById(sessionFactory, Product.class, 2L));

        long id1 = 1L;
        long id2 = 2L;
        EntityUtil.deleteEntityById(Customer.class, id1);
        logger.info("Customer with id: {} deleted", id1);

        EntityUtil.deleteEntityById(Product.class, id2);
        logger.info("Product with id: {} deleted", id2);
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
