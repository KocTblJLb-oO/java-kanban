package ru.moysayt.steptraker.model;

import ru.moysayt.steptraker.service.directory.TypeOfTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int id;
    private final String title;
    private final String text;
    private StatusOfTask status;
    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;

    public Task(String title, String text, StatusOfTask status) {
        this.title = title;
        this.text = text;
        this.status = status;
    }

    public Task(String title, String text, StatusOfTask status, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.text = text;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = setEndTime(startTime, duration);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(StatusOfTask status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime newStartTime, Duration dr) {
        this.startTime = newStartTime;
        endTime = setEndTime(startTime, dr);
    }

    public void setDuration(Duration newDuration) {
        this.duration = newDuration;
        endTime = setEndTime(startTime, duration);
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

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "ID - " + id + " Задача: " + title + " Статус: " + status + "\nОписание: " + text
                + "\nНачало: " + startTime + " Продолжительность: " + duration + " Окончание: " + endTime
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

    public TypeOfTask getTypeOfTask() {
        return TypeOfTask.TASK;
    }

    private LocalDateTime setEndTime(LocalDateTime st, Duration dur) {
        return st.plus(dur);
    }

    public void setEndTime(LocalDateTime et) {
        endTime = et;
    }
}
