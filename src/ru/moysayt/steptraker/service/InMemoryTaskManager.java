package ru.moysayt.steptraker.service;

import ru.moysayt.steptraker.manager.Managers;
import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.history.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int taskId = 0;
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicList = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    public HistoryManager<Task> historyManager = Managers.getDefaultHistory();

    /*
------------------------------------------------ МЕТОДЫ ТАСК МЕНЕДЖЕРА
 */

    private int getNewTaskId() {
        taskId++;
        return taskId;
    }

    /*
    Добавил метод, чтобы можно было протестировать:
    "проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера"
     */
    public int getId() {
        return getNewTaskId();
    }

    //  Возвращает историю просмотров
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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
        return new ArrayList<>(taskList.values());
    }

    // Удаление всех задач
    @Override
    public void clearTaskList() {
        for (int taskId : taskList.keySet()) { // Удаление всх задач из истории
            historyManager.remove(taskId);
        }

        taskList.clear();
    }

    // Получение задачи по ID
    @Override
    public Task getTaskByID(int id) {
        final Task task = taskList.get(id);
        historyManager.addHistory(task);
        return task;
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
        historyManager.remove(id); // Удаление из истории
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
        return new ArrayList<>(epicList.values());
    }

    // Удаление всех эпиков
    @Override
    public void clearEpicList() {
        for (int epicId : epicList.keySet()) { // Удаление всх эпиков из истории
            ArrayList<Integer> subtasks = epicList.get(epicId).getEpicSubtask();

            for (int subtaskId : subtasks) { // Удаление всех сабтасков эпика из истории
                historyManager.remove(subtaskId);
            }

            historyManager.remove(epicId);
        }

        epicList.clear();
        subtaskList.clear(); // Удаляем все подзадачи
    }

    // Получение эпика по ID
    @Override
    public Epic getEpicByID(int id) {
        final Epic epic = epicList.get(id);
        historyManager.addHistory(epic);
        return epic;
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
        historyManager.remove(id); // Удаление из истории
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
        return new ArrayList<>(subtaskList.values());
    }

    // Удаление всех подзадач
    @Override
    public void clearSubtaskList() {

        for (int subtaskId : subtaskList.keySet()) { // Удаление всх сабтасков из истории
            historyManager.remove(subtaskId);
        }

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
        final Subtask subtask = subtaskList.get(id);
        historyManager.addHistory(subtask);
        return subtask;
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
        historyManager.remove(id); // Удаление из истории
    }

}