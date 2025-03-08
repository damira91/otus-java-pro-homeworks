package ru.otus.kudaiberdieva.homework14.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.kudaiberdieva.homework14.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
