package ru.otus.kudaiberdieva.homework07;

import ru.otus.kudaiberdieva.homework07.database.DBMigrator;
import ru.otus.kudaiberdieva.homework07.database.DataSource;
import ru.otus.kudaiberdieva.homework07.entity.Account;
import ru.otus.kudaiberdieva.homework07.entity.User;
import ru.otus.kudaiberdieva.homework07.repositories.AbstractRepository;


public class Application {
    private static DataSource dataSource;

    public static void main(String[] args) {
        try {
            dataSource = new DataSource("jdbc:h2:file:./db;MODE=PostgreSQL");
            dataSource.connect();

            DBMigrator dbMigrator = new DBMigrator(dataSource);
            dbMigrator.migrate("init.sql");

            AbstractRepository<User> userRepository = new AbstractRepository<>(dataSource, User.class);
            User user = new User("bob1", "1231", "bob1");
            User user2 = new User("dan", "456", "dan");
            User updatedUser = new User(72L, "sam", "789", "sam");
            userRepository.create(user);
            userRepository.create(user2);


            System.out.println(userRepository.findAll());
            System.out.println(userRepository.findById(72L));
            userRepository.update(updatedUser);
            System.out.println(userRepository.findById(72L));
            userRepository.deleteById(75L);
            userRepository.findById(75L);
            userRepository.deleteAll();

            AbstractRepository<Account> accountRepository = new AbstractRepository<>(dataSource, Account.class);
            Account account = new Account(100L, "credit", "blocked");
            Account updatedAccount = new Account(1L, 200L, "credit", "opened");
            accountRepository.create(account);
            System.out.println(accountRepository.findAll());
            System.out.println(accountRepository.findById(1L));
            accountRepository.update(updatedAccount);
            System.out.println(userRepository.findById(1L));
            accountRepository.deleteById(6L);
            accountRepository.findById(6L);
            accountRepository.deleteAll();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dataSource != null) {
                dataSource.disconnect();
            }
        }
    }
}