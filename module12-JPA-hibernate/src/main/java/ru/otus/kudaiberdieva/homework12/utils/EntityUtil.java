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
    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = SessionFactoryConfiguration.init();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + e);
        }
    }

    public static <T> T insert(SessionFactory sessionFactory, T entity) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            T merged = (T) session.merge(entity);
            session.getTransaction().commit();

            return merged;
        }
    }

    public static <T> T findById(SessionFactory sessionFactory, Class<T> cls, long id) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            T entity = session.get(cls, id);
            session.getTransaction().commit();
            return entity;
        } finally {
            session.close();
        }
    }


    public static <T> List<T> findAll(SessionFactory sessionFactory, Class<T> cls) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            List<T> entities = session.createQuery("SELECT a FROM " + cls.getSimpleName() + " a", cls)
                    .getResultList();
            session.getTransaction().commit();
            return entities;
        } catch (Exception ex) {
            throw ex;
        } finally {
            session.close();
        }
    }

    public static <T> void deleteEntityById(Class<T> entityClass, Long entityId) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            T entity = session.get(entityClass, entityId);
            if (entity != null) {
                session.remove(entity);
            } else {
                throw new IllegalArgumentException("Entity with id " + entityId + " not found for class " + entityClass.getName());
            }
            session.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            session.close();
        }
    }
}

