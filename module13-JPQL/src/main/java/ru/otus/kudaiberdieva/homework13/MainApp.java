package ru.otus.kudaiberdieva.homework13;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import ru.otus.kudaiberdieva.homework13.entities.Address;
import ru.otus.kudaiberdieva.homework13.entities.Client;
import ru.otus.kudaiberdieva.homework13.entities.Phone;


public class MainApp {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SingleUnit");
        EntityManager entityManager = emf.createEntityManager();
        Client client = new Client("Sherlock Holmes");
        Address address = new Address("Baker 221B");
        client.setAddress(address);
        Phone phone1 = new Phone("52856897258");
        Phone phone2 = new Phone("65464878979");
        phone1.setClient(client);
        phone2.setClient(client);
        client.getPhones().add(phone1);
        client.getPhones().add(phone2);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(client);
            entityManager.persist(phone1);
            entityManager.persist(phone2);
            entityManager.persist(address);
            transaction.commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}
