package ru.moysayt.steptraker.service;

import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int taskId = 0;
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    /*
------------------------------------------------ МЕТОДЫ ТАСК МЕНЕДЖЕРА
 */

    public int getNewTaskId() {
        taskId++;
        return taskId;
    }

/*
------------------------------------------------ РАБОТА С ЗАДАЧАМИ
 */

    // Создание задачи
    public void createTask(Task task) {
        int id = getNewTaskId();
        task.setId(id);
        taskList.put(id, task);
    }

/*    // Показать все задачи
    public void showAllTask() {
        // Пользовательский вывод убрал, а вот про перенос в Main похоже на ошибку - он же не будет работать.
        for (Task task : taskList.values()) {
            System.out.println(task);
        }
    }*/

    // Получение всех задач
    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(taskList.values());
    }

    // Удаление всех задач
    public void clearTaskList() {
        taskList.clear();
    }

    // Получение задачи по ID
    public Task getTaskByID(int id) {
        return taskList.get(id);
    }

    // Обновление задачи
    public void updateTask(Task task) {
        taskList.put(task.getId(), task);
    }

    // Удаление задачи
    public void deleteTask(int id) {
        taskList.remove(id);
    }

    /*
------------------------------------------------ РАБОТА С ЭПИКАМИ
 */

    // Создание эпика
    public void createEpic(Epic epic) {
        int id = getNewTaskId();
        epic.setId(id);
        epicList.put(id, epic);
    }

    /*    // Показать все задачи
        public void showAllEpic() {
            for (Epic epic : epicList.values()) {
                System.out.println(epic);
            }
        }*/

    // Получение всех эпиков
    public ArrayList<Epic> getEpics() {
        return new ArrayList<Epic>(epicList.values());
    }


    // Удаление всех эпиков
    public void clearEpicList() {
        epicList.clear();
        subtaskList.clear(); // Удаляем все подзадачи
    }

    // Получение эпика по ID
    public Epic getEpicByID(int id) {
        return epicList.get(id);
    }

    // Обновление эпика
    public void updateEpic(Epic epic) {
        epicList.put(epic.getId(), epic);
    }

    // Удаление эпика
    public void deleteEpic(int id) {
        epicList.get(id).deleteAllSubtask();
        epicList.remove(id);
    }

    // Получение подзадач эпика
    public ArrayList<Subtask> getEpicSubtask(int epicId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        ArrayList<Integer> subtasksId = epicList.get(epicId).getEpicSubtask();
        for (int subtaskId : subtasksId) {
            subtasks.add(subtaskList.get(subtaskId));
        }
        return subtasks;
    }

    // Обновление статуса эпика
    public void setStatusEpic(Epic epic) {
        StatusOfTask status;
        int all = 0;
        int newSub = 0;
        int doneSub = 0;

        for (int subtaskID : epic.getEpicSubtask()) {
            status = subtaskList.get(subtaskID).getStatus();
            all++;

            if (status == StatusOfTask.NEW || all == 0) {
                newSub++;
            } else if (status == StatusOfTask.DONE) {
                doneSub++;
            }

            if (all == newSub) {
                epic.setStatus(StatusOfTask.NEW);
            } else if (all == doneSub) {
                epic.setStatus(StatusOfTask.DONE);
            } else {
                epic.setStatus(StatusOfTask.IN_PROGRESS);
            }
        }
    }

    /*
------------------------------------------------ РАБОТА С ПОДЗАДАЧАМИ
 */

    // Создание подзадачи
    public void createSubtask(int epicId, Subtask subtask) {
        int id = getNewTaskId();
        subtask.setId(id);
        subtaskList.put(id, subtask);
        epicList.get(epicId).addSubtask(id); // Добавляем id подзадачи в эпик
        setStatusEpic(epicList.get(epicId)); // И обновляем статус
    }

    /*// Показать все подзадачи
    public void showAllSubtask() {

        for (Subtask subtask : subtaskList.values()) {
            System.out.println(subtask);
        }
    }*/

    // Получение всех подзадач
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<Subtask>(subtaskList.values());
    }

    // Удаление всех подзадач
    public void clearSubtaskList() {
        subtaskList.clear();
        // Обновление статуса всех эпиков
        for (Epic epic : epicList.values()) { // Удаляем все подзадачи в эпиках и обновляем статусы
            epic.deleteAllSubtask();
            setStatusEpic(epicList.get(epic.getId()));
        }
    }

    // Получение подзадачи по ID
    public Subtask getSubtaskByID(int id) {
        return subtaskList.get(id);
    }

    // Обновление подзадачи
    public void updateSubtask(Subtask subtask) {
        subtaskList.put(subtask.getId(), subtask);
        setStatusEpic(epicList.get(subtask.getParentId()));
    }

    // Удаление подзадачи
    public void deleteSubtask(int id) {
        int parentEpicId = subtaskList.get(id).getParentId();
        Epic epic = epicList.get(parentEpicId);
        epic.deleteSubtask(id);
        setStatusEpic(epic);
        subtaskList.remove(id);
    }
}