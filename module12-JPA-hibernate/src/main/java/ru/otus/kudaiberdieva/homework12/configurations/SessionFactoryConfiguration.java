package ru.otus.kudaiberdieva.homework12.configurations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.kudaiberdieva.homework12.entities.Customer;
import ru.otus.kudaiberdieva.homework12.entities.Product;
import ru.otus.kudaiberdieva.homework12.utils.EntityUtil;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionFactoryConfiguration {
    private static SessionFactory sessionFactory;

    public static SessionFactory init() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration()
                        .configure("/hibernate.cfg.xml")
                        .addAnnotatedClass(Customer.class)
                        .addAnnotatedClass(Product.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);

                // Инициализация данных
                initData(sessionFactory);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    private static void initData(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Customer customer1 = EntityUtil.insert(sessionFactory, new Customer("Dean"));
                Customer customer2 = EntityUtil.insert(sessionFactory, new Customer("Sam"));
                Customer customer3 = EntityUtil.insert(sessionFactory, new Customer("John"));
                Customer customer4 = EntityUtil.insert(sessionFactory, new Customer("Mary"));
                Customer customer5 = EntityUtil.insert(sessionFactory, new Customer("Jane"));

                EntityUtil.insert(sessionFactory, new Product("Bread", 15.00, Set.of(customer2, customer3)));
                EntityUtil.insert(sessionFactory, new Product("Water", 25.00, Set.of(customer1, customer4, customer5)));
                EntityUtil.insert(sessionFactory, new Product("Milk", 40.00, Set.of(customer3, customer5)));

                transaction.commit();
            } catch (Exception ex) {
                if (transaction != null) {
                    transaction.rollback();
                }
                ex.printStackTrace();
            }
        }
    }
}

