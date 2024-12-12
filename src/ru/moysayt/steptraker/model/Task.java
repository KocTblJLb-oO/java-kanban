package ru.moysayt.steptraker.model;

import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String text;
    private StatusOfTask status;

    public Task(String title, String text, StatusOfTask status) {
        this.title = title;
        this.text = text;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(StatusOfTask status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public StatusOfTask getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "ID - " + id + " Задача: " + title + " Статус: " + status + "\nОписание: " + text
                + "\n------------------------------------------------";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(text, task.text) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text, status);
    }

}
