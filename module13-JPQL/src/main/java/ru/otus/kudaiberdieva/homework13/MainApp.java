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
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("SingleUnit");
             EntityManager entityManager = emf.createEntityManager()) {

            Client client = new Client("Sherlock Holmes");
            Address address = new Address("Baker 221B");
            client.setAddress(address);
            client.addPhone(new Phone("52856897258"));
            client.addPhone(new Phone("65464878979"));

            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                entityManager.persist(client);
                entityManager.persist(address);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }
}
