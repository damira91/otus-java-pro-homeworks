package ru.otus.kudaiberdieva.homework07.entity;

import ru.otus.kudaiberdieva.homework07.annotations.RepositoryField;
import ru.otus.kudaiberdieva.homework07.annotations.RepositoryIdField;
import ru.otus.kudaiberdieva.homework07.annotations.RepositoryTable;

@RepositoryTable(title = "accounts")
public class Account {
    @RepositoryIdField(name = "id")
    @RepositoryField(name = "")
    private Long id;

    @RepositoryField(name = "")
    private Long amount;

    @RepositoryField(name = "tp")
    private String tp;

    @RepositoryField(name = "status")
    private String status;

    public Account(Long id, Long amount, String tp, String status) {
        this.id = id;
        this.amount = amount;
        this.tp = tp;
        this.status = status;
    }

    public Account() {
    }

    public Account(Long amount, String tp, String status) {
        this.amount = amount;
        this.tp = tp;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", amount=" + amount +
                ", tp='" + tp + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}