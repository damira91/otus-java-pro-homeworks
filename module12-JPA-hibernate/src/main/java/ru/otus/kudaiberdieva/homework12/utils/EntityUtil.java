package ru.otus.kudaiberdieva.homework12.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.kudaiberdieva.homework12.configurations.SessionFactoryConfiguration;
import ru.otus.kudaiberdieva.homework12.entities.Customer;
import ru.otus.kudaiberdieva.homework12.entities.Product;

import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityUtil {
    private static final SessionFactory sessionFactory = SessionFactoryConfiguration.getSessionFactory();

    public static <T> T insert(SessionFactory sessionFactory, T entity) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            T merged = (T) session.merge(entity);
            session.getTransaction().commit();

            return merged;
        }
    }
    public static <T> T findById(SessionFactory sessionFactory, Class<T> cls, long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            return session.get(cls, id);
        }
    }


    public static <T> List<T> findAll(SessionFactory sessionFactory, Class<T> cls) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            return session.createQuery("SELECT a FROM " + cls.getSimpleName() + " a", cls)
                    .getResultList();
        }
    }

    public static <T> void deleteEntityById(Class<T> entityClass, Long entityId) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            T entity = session.get(entityClass, entityId);
            if (entity != null) {
                session.remove(entity);
            } else {
                throw new IllegalArgumentException("Entity with id " + entityId + " not found for class " + entityClass.getName());
            }

            session.getTransaction().commit();
        }
    }
}

