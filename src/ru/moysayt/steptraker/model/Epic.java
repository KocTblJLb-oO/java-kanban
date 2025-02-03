package ru.moysayt.steptraker.model;

import ru.moysayt.steptraker.service.directory.TypeOfTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String title, String text, StatusOfTask status) {
        super(title, text, status);
    }

    public Epic(String title, String text, StatusOfTask status, LocalDateTime startTime,
                Duration duration) {
        super(title, text, status, startTime, duration);
    }

    public void addSubtask(int idSubtask) {
        subTaskIds.add(idSubtask);
    }

    public void deleteAllSubtask() {
        subTaskIds.clear();
    }

    public void deleteSubtask(int idSubtask) {
        subTaskIds.remove(Integer.valueOf(idSubtask));
    }

    public ArrayList<Integer> getEpicSubtask() {
        return subTaskIds;
    }

    @Override
    public String toString() {
        return "ID - " + getId() + " Эпик: " + getTitle() + " Статус: " + getStatus() + "\nОписание: " + getText()
                + "\nНачало: " + getStartTime() + " Продолжительность: " + getDuration() + " Окончание: " + getEndTime()
                + "\nПодзадачи:" + subTaskIds + "\n------------------------------------------------";
    }

    @Override
    public TypeOfTask getTypeOfTask() {
        return TypeOfTask.EPIC;
    }
}
