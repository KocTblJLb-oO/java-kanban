package ru.moysayt.steptraker.model;

import ru.moysayt.steptraker.service.directory.TypeOfTask;

public class Subtask extends Task {
    private final int parentId;

    public Subtask(int parentEpicID, String title, String text, StatusOfTask status) {
        super(title, text, status);
        this.parentId = parentEpicID;
    }

    public int getParentId() {
        return parentId;
    }

    @Override
    public String toString() {
        return "ID - " + getId() + " Подзадача: " + getTitle() + " Статус: " + getStatus() + "\nОписание: " + getText()
                + "\nЭпик: " + parentId + "\n------------------------------------------------";
    }

    @Override
    public TypeOfTask getTypeOfTask() {
        return TypeOfTask.SUBTASK;
    }
}