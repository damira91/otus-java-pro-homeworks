package ru.otus.kudaiberdieva.homework14.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.kudaiberdieva.homework14.dtos.UserDTO;
import ru.otus.kudaiberdieva.homework14.entities.User;
import ru.otus.kudaiberdieva.homework14.exceptions.EntityException;
import ru.otus.kudaiberdieva.homework14.repositories.UserRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAllUsers() {
        return StreamSupport.stream(userRepo.findAll().spliterator(), false)
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .phone(user.getPhone()).build()).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityException(String.format("There is no user with id: %s", id)));
        return UserDTO.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).phone(user.getPhone()).build();
    }

    @Override
    @Transactional
    public void createUser(UserDTO userdto) {
        userRepo.save(User.builder().name(userdto.getName()).email(userdto.getEmail()).phone(userdto.getPhone()).build());
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
