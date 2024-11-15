package ru.moysayt.steptraker.service;

import ru.moysayt.steptraker.manager.Managers;
import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.history.HistoryManager;
import ru.moysayt.steptraker.service.history.InMemoryHistoryManager;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int taskId = 0;
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    public HistoryManager historyManager = Managers.getDefaultHistory();

    /*
------------------------------------------------ МЕТОДЫ ТАСК МЕНЕДЖЕРА
 */
    @Override
    public int getNewTaskId() {
        taskId++;
        return taskId;
    }

/*
------------------------------------------------ РАБОТА С ЗАДАЧАМИ
 */

    // Создание задачи
    @Override
    public void createTask(Task task) {
        int id = getNewTaskId();
        task.setId(id);
        taskList.put(id, task);
    }

    // Получение всех задач
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(taskList.values());
    }

    // Удаление всех задач
    @Override
    public void clearTaskList() {
        taskList.clear();
    }

    // Получение задачи по ID
    @Override
    public Task getTaskByID(int id) {
        historyManager.addHistory(taskList.get(id));
        return taskList.get(id);
    }

    // Обновление задачи
    @Override
    public void updateTask(Task task) {
        taskList.put(task.getId(), task);
    }

    // Удаление задачи
    @Override
    public void deleteTask(int id) {
        taskList.remove(id);
    }

    /*
------------------------------------------------ РАБОТА С ЭПИКАМИ
 */

    // Создание эпика
    @Override
    public void createEpic(Epic epic) {
        int id = getNewTaskId();
        epic.setId(id);
        epicList.put(id, epic);
    }

    // Получение всех эпиков
    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<Epic>(epicList.values());
    }

    // Удаление всех эпиков
    @Override
    public void clearEpicList() {
        epicList.clear();
        subtaskList.clear(); // Удаляем все подзадачи
    }

    // Получение эпика по ID
    @Override
    public Epic getEpicByID(int id) {
        historyManager.addHistory(epicList.get(id));
        return epicList.get(id);
    }

    // Обновление эпика
    @Override
    public void updateEpic(Epic epic) {
        epicList.put(epic.getId(), epic);
    }

    // Удаление эпика
    @Override
    public void deleteEpic(int id) {
        epicList.get(id).deleteAllSubtask();
        epicList.remove(id);
    }

    // Получение подзадач эпика
    @Override
    public ArrayList<Subtask> getEpicSubtask(int epicId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        ArrayList<Integer> subtasksId = epicList.get(epicId).getEpicSubtask();
        for (int subtaskId : subtasksId) {
            subtasks.add(subtaskList.get(subtaskId));
        }
        return subtasks;
    }

    // Обновление статуса эпика
    private void setStatusEpic(Epic epic) {
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
    @Override
    public void createSubtask(int epicId, Subtask subtask) {
        int id = getNewTaskId();
        subtask.setId(id);
        subtaskList.put(id, subtask);
        epicList.get(epicId).addSubtask(id); // Добавляем id подзадачи в эпик
        setStatusEpic(epicList.get(epicId)); // И обновляем статус
    }

    // Получение всех подзадач
    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<Subtask>(subtaskList.values());
    }

    // Удаление всех подзадач
    @Override
    public void clearSubtaskList() {
        subtaskList.clear();
        // Обновление статуса всех эпиков
        for (Epic epic : epicList.values()) { // Удаляем все подзадачи в эпиках и обновляем статусы
            epic.deleteAllSubtask();
            setStatusEpic(epicList.get(epic.getId()));
        }
    }

    // Получение подзадачи по ID
    @Override
    public Subtask getSubtaskByID(int id) {
        historyManager.addHistory(subtaskList.get(id));
        return subtaskList.get(id);
    }

    // Обновление подзадачи
    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskList.put(subtask.getId(), subtask);
        setStatusEpic(epicList.get(subtask.getParentId()));
    }

    // Удаление подзадачи
    @Override
    public void deleteSubtask(int id) {
        int parentEpicId = subtaskList.get(id).getParentId();
        Epic epic = epicList.get(parentEpicId);
        epic.deleteSubtask(id);
        setStatusEpic(epic);
        subtaskList.remove(id);
    }

}