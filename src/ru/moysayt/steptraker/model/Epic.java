package ru.moysayt.steptraker.model;

import ru.moysayt.steptraker.service.TaskManager;

import java.util.ArrayList;

public class Epic extends Task {
    /*private int id;
    private String title;
    private String text;
    private StatusOfTask status = StatusOfTask.NEW;*/
    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String title, String text, StatusOfTask status) {
        super(title, text, status);
       /* this.title = title;
        this.text = text;*/
    }

   /* public void setId(int id){ // Работает только паблик, а это странно для ID
        this.id = id;
    }*/

    public void addSubtask(int idSubtask) {
        subTaskIds.add(idSubtask);
    }

    public void deleteAllSubtask() {
        subTaskIds.clear();
    }

    public void deleteSubtask(int idSubtask) {
        subTaskIds.remove(idSubtask);
    }

    public ArrayList<Integer> getEpicSubtask() {
        return subTaskIds;
    }

    @Override
    public String toString() {
        return "ID - " + getId() + " Эпик: " + getTitle() + " Статус: " + getStatus() + "\nОписание: " + getText()
                + "\nПодзадачи:" + subTaskIds + "\n------------------------------------------------";
    }
}
