package ru.otus.kudaiberdieva.homework12.configurations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.kudaiberdieva.homework12.entities.Customer;
import ru.otus.kudaiberdieva.homework12.entities.Product;
import ru.otus.kudaiberdieva.homework12.utils.EntityUtil;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionFactoryConfiguration {

    public static SessionFactory getSessionFactory() {
        try {
            Configuration configuration = new Configuration()
                    .configure("/hibernate.cfg.xml")
                    .addAnnotatedClass(Customer.class)
                    .addAnnotatedClass(Product.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void init(SessionFactory session) {

        Customer customer1 = EntityUtil.insert(session, new Customer("Dean"));
        Customer customer2 = EntityUtil.insert(session, new Customer("Sam"));
        Customer customer3 = EntityUtil.insert(session, new Customer("John"));
        Customer customer4 = EntityUtil.insert(session, new Customer("Mary"));
        Customer customer5 = EntityUtil.insert(session, new Customer("Jane"));

        EntityUtil.insert(session, new Product("Bread", 15.00, Set.of(customer2, customer3)));
        EntityUtil.insert(session, new Product("Water", 25.00, Set.of(customer1, customer4, customer5)));
        EntityUtil.insert(session, new Product("Milk", 40.00, Set.of(customer3, customer5)));
    }
}
