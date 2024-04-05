package ru.otus.kudaiberdieva.homework3;


public class Task {
    private int id;
    private String title;
    private Status status;

    public int getId() {
        return id;
    }
    public Task setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }
    public Task setTitle(String title) {
        this.title = title;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Task setStatus(Status status) {
        this.status = status;
        return this;
    }
    public Task() {
    }
}
