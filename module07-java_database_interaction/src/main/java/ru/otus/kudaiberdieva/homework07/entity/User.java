package ru.otus.kudaiberdieva.homework07.entity;

import ru.otus.kudaiberdieva.homework07.annotations.RepositoryColumn;
import ru.otus.kudaiberdieva.homework07.annotations.RepositoryField;
import ru.otus.kudaiberdieva.homework07.annotations.RepositoryIdField;
import ru.otus.kudaiberdieva.homework07.annotations.RepositoryTable;

@RepositoryTable(title = "users")
public class User {
    @RepositoryIdField
    @RepositoryField
    @RepositoryColumn(name = "id")
    private Long id;

    @RepositoryField
    @RepositoryColumn(name = "login")
    private String login;

    @RepositoryField
    @RepositoryColumn(name = "password")
    private String password;

    @RepositoryField
    @RepositoryColumn(name = "nickname")
    private String nickname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User() {
    }

    public User(String login, String password, String nickname) {
        this.login = login;
        this.password = password;
        this.nickname = nickname;
    }

    public User(Long id, String login, String password, String nickname) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}