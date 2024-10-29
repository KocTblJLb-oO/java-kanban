package ru.moysayt.steptraker.service;

import ru.moysayt.steptraker.model.Epic;
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

    // Показать все задачи
    public void showAllTask() {
        // Пользовательский вывод убрал, а вот про перенос в Main похоже на ошибку - он же не будет работать.
        for (Task task : taskList.values()) {
            System.out.println(task);
        }
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
    public void updateTask(int taskId, Task task) {
        task.setId(taskId);
        taskList.put(taskId, task);
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

    // Показать все задачи
    public void showAllEpic() {
        for (Epic epic : epicList.values()) {
            System.out.println(epic);
        }
    }

    // Удаление всех эпиков
    public void clearEpicList() {
        epicList.clear();
    }

    // Получение эпика по ID
    public Epic getEpicByID(int id) {
        return epicList.get(id);
    }

    // Обновление эпика
    public void updateEpic(int epicId, Epic epic) {
        epic.setId(epicId);
        epicList.put(epicId, epic);
    }

    // Удаление эпика
    public void deleteEpic(int id) {
        // Удаление подзадач этого эпика
        for (Epic epic : epicList.values()) {
            epic.deleteAllSubtask();
        }
        epicList.remove(id);
    }

    public ArrayList<Subtask> getEpicSubtask(int epicId){
        ArrayList<Subtask> subtasks = new ArrayList<>();
        ArrayList<Integer> subtasksId = epicList.get(epicId).getEpicSubtask();
        for (int subtaskId : subtasksId) {
            subtasks.add(subtaskList.get(subtaskId));
        }
        return subtasks;
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
    }

    // Показать все подзадачи
    public void showAllSubtask() {

        for (Subtask subtask : subtaskList.values()) {
            System.out.println(subtask);
        }
    }

    // Удаление всех подзадач
    public void clearSubtaskList() {
        subtaskList.clear();
        // Обновление статуса всех эпиков
        for (Epic epic : epicList.values()) {
            epic.deleteAllSubtask();
            epic.setStatusEpic(this);
        }
    }

    // Получение подзадачи по ID
    public Subtask getSubtaskByID(int id) {
        return subtaskList.get(id);
    }

    // Обновление подзадачи
    public void updateSubtask(int subtaskId, Subtask subtask) {
        subtask.setId(subtaskId);
        subtaskList.put(subtaskId, subtask);
        epicList.get(subtask.getParentId()).setStatusEpic(this);
    }

    // Удаление подзадачи
    public void deleteSubtask(int id) {
        int parentEpicId = subtaskList.get(id).getParentId();
        Epic epic = epicList.get(parentEpicId);
        epic.deleteSubtask(id, this);
        subtaskList.remove(id);
    }
}