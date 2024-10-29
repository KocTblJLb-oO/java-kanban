package ru.moysayt.steptraker.model;

import ru.moysayt.steptraker.service.TaskManager;

import java.util.ArrayList;

public class Epic extends Task {
    private int id;
    private String title;
    private String text;
    private StatusOfTask status = StatusOfTask.NEW;
    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String title, String text, StatusOfTask status) {
        super(title, text, status);
        this.title = title;
        this.text = text;
    }

    public void setId(int id){ // Работает только паблик, а это странно для ID
        this.id = id;
    }

    public void addSubtask(int idSubtask) {
        subTaskIds.add(idSubtask);
    }

    public void deleteAllSubtask() {
        subTaskIds.clear();
    }

    public void deleteSubtask(int idSubtask, TaskManager task) {
        subTaskIds.remove(idSubtask);
        setStatusEpic(task);
    }

    public ArrayList<Integer> getEpicSubtask() {
        return subTaskIds;
    }

    public void setStatusEpic(TaskManager task) {
        StatusOfTask status;
        int all = 0;
        int newSub = 0;
        int done = 0;

        for (int subtaskID : subTaskIds) {
            status = task.getSubtaskByID(subtaskID).getStatus();
            all++;

            if (status == StatusOfTask.NEW || all == 0) {
                newSub++;
            } else if (status == StatusOfTask.DONE) {
                done++;
            }

            if (all == newSub) {
                this.status = StatusOfTask.NEW;
            } else if (all == done) {
                this.status = StatusOfTask.DONE;
            } else {
                this.status = StatusOfTask.IN_PROGRESS;
            }
        }
    }

    @Override
    public String toString() {
        return "ID - " + id + " Эпик: " + title + " Статус: " + status + "\nОписание: " + text + "\nПодзадачи:"
                + subTaskIds + "\n------------------------------------------------";
    }
}
