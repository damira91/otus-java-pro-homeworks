package ru.otus.kudaiberdieva.homework14.services;

import ru.otus.kudaiberdieva.homework14.dtos.UserDTO;


import java.util.List;

public interface UserService {
    List<UserDTO> findAllUsers();

    UserDTO findUserById(Long id);

    void createUser(UserDTO user);

    void deleteUser(Long id);
}
