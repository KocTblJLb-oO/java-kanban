package ru.moysayt.steptraker.service;

import ru.moysayt.steptraker.manager.Managers;
import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.directory.ManagerSaveException;
import ru.moysayt.steptraker.service.directory.NotFoundException;
import ru.moysayt.steptraker.service.history.HistoryManager;

import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int taskId = 0;
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicList = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    public HistoryManager<Task> historyManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(
                    Task::getStartTime,
                    Comparator.nullsLast(Comparator.naturalOrder())
            ).thenComparing(Task::getId)
    );

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

    // При восстановлении задач из файла необходимо установить счётчик ID в соответствии с файлом
    public void setStartId(int i) {
        taskId = i;
    }

    // Добавляем Таски в список по приоритету
    private void addInPrioritizedTasksTreeSet(Task task) {
        prioritizedTasks.add(task);
    }

    //возвращающий список задач и подзадач в заданном порядке
    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    // Проверяет две задачи на пересечение по времени
    private boolean isConflictTwoTask(Task t1, Task t2) {
        if (t1.getStartTime() == null || t2.getStartTime() == null) {
            return false;
        }
        return t1.getStartTime().isBefore(t2.getStartTime()) && t1.getEndTime().isAfter(t2.getStartTime()) // Старт t2 на отрезке t1
                || t1.getStartTime().isBefore(t2.getEndTime()) && t1.getEndTime().isAfter(t2.getEndTime()); // Конец t2 на отрезке t1
    }

    private void checkConflictTime(Task task) {
        boolean isConflict = prioritizedTasks.stream() // Проверка задач на пересечение по времени
                .anyMatch(task2 -> isConflictTwoTask(task2, task));
        if (isConflict) {
            throw new ManagerSaveException("Время выполнения задачи конфликтует с уже имеющейся");
        }
    }

/*
------------------------------------------------ РАБОТА С ЗАДАЧАМИ
 */

    // Создание задачи
    @Override
    public void createTask(Task task) {
        checkConflictTime(task);

        int id = getNewTaskId();
        task.setId(id);
        taskList.put(id, task);
        addInPrioritizedTasksTreeSet(task);
    }

    // Получение всех задач
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskList.values());
    }

    // Удаление всех задач
    @Override
    public void clearTaskList() {
        taskList.values().stream()
                .map(Task::getId)
                .forEach(historyManager::remove); // Удаление всех задач из истории
        taskList.values().stream()
                .filter(task -> task.getStartTime() != null)
                .forEach(prioritizedTasks::remove); // Удаление из списка по приоритету

        taskList.clear();
    }

    // Получение задачи по ID
    @Override
    public Task getTaskByID(int id) {
        final Task task = taskList.get(id);
        historyManager.addHistory(task);

        if (task == null) {
            throw new NotFoundException("Задача не найдена");
        }

        return task;
    }

    // Обновление задачи
    @Override
    public void updateTask(Task task) {
        checkConflictTime(task);

        taskList.put(task.getId(), task);
        addInPrioritizedTasksTreeSet(task);
    }

    // Удаление задачи
    @Override
    public void deleteTask(int id) {
        prioritizedTasks.remove(taskList.get(id)); // Удаление из списка по приоритету
        taskList.remove(id);
        historyManager.remove(id); // Удаление из истории
    }

    // Метод добавляет задачу в список. Необходим для восстановления задач из файла
    public void addTaskInTaskList(Task task) {
        taskList.put(task.getId(), task);
        addInPrioritizedTasksTreeSet(task); // Добавляем в список по приоритету
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
        epicList.values().stream()
                .map(Task::getId)
                .forEach(historyManager::remove); // Удаление всех эпиков из истории
        subtaskList.values().stream()
                .map(Task::getId)
                .forEach(historyManager::remove); // Удаление всех сабтасков из истории
        epicList.values().stream()
                .map(Epic::getEpicSubtask)
                .forEach(subList -> {
                    for (int i : subList) {
                        prioritizedTasks.remove(this.getSubtaskByID(i));
                    }
                }); // Удаляем все сабтаски эпиков из списка по приоритету

        epicList.clear();
        subtaskList.clear(); // Удаляем все подзадачи
    }

    // Получение эпика по ID
    @Override
    public Epic getEpicByID(int id) {
        final Epic epic = epicList.get(id);

        if (epic == null) {
            throw new NotFoundException("Эпик не найден");
        }

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
        historyManager.remove(id); // Удаление из истории

        ArrayList<Integer> subtasks = epicList.get(id).getEpicSubtask(); // Удаление из истории сабтасков эпика
        for (int subtaskId : subtasks) {
            historyManager.remove(subtaskId);
        }

        for (Subtask subtask : subtaskList.values()) { // Удаляем сабтаски
            if (id == subtask.getParentId()) {
                subtaskList.remove(subtask.getId());
            }
        }

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

    // Устанавливает время начала, окончания и продолжительность эпика
    private void setTimeEpic(Epic epic) {
        Optional<Map.Entry<Integer, Subtask>> startTimeSubtaskOptional = subtaskList.entrySet().stream()
                .filter(mapEntry -> mapEntry.getValue().getStartTime() != null).min(Map.Entry.comparingByValue(
                        Comparator.comparing(Subtask::getStartTime)));
        Optional<Map.Entry<Integer, Subtask>> endTimeSubtaskOptional = subtaskList.entrySet().stream()
                .filter(mapEntry -> mapEntry.getValue().getStartTime() != null)
                .max(Map.Entry.comparingByValue(Comparator.comparing(Subtask::getEndTime)));
        Optional<Duration> sumDurationSubtask = subtaskList.values().stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .map(Task::getDuration)
                .reduce((Duration::plus));

        if (startTimeSubtaskOptional.isPresent() && endTimeSubtaskOptional.isPresent() && sumDurationSubtask.isPresent()) {
            epic.setStartTime(startTimeSubtaskOptional.get().getValue().getStartTime(), startTimeSubtaskOptional.get()
                    .getValue().getDuration());
            epic.setEndTime(endTimeSubtaskOptional.get().getValue().getEndTime());
            epic.setDuration(sumDurationSubtask.get());
        }
    }

    // Метод добавляет эпик в список. Необходим для восстановления задач из файла
    public void addEpicInEpicList(Epic epic) {
        epicList.put(epic.getId(), epic);
    }

    /*
------------------------------------------------ РАБОТА С ПОДЗАДАЧАМИ
 */

    // Создание подзадачи
    @Override
    public void createSubtask(int epicId, Subtask subtask) {
        checkConflictTime(subtask);

        int id = getNewTaskId();
        subtask.setId(id);
        subtaskList.put(id, subtask);
        epicList.get(epicId).addSubtask(id); // Добавляем id подзадачи в эпик
        Epic parrentEpic = epicList.get(epicId);
        setStatusEpic(parrentEpic); // И обновляем статус
        setTimeEpic(parrentEpic); // Обновляем дату и время
        addInPrioritizedTasksTreeSet(subtask); // Добавляем в список по приоритету
    }

    // Получение всех подзадач
    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskList.values());
    }

    // Удаление всех подзадач
    @Override
    public void clearSubtaskList() {
        subtaskList.values().stream()
                .map(Task::getId)
                .forEach(historyManager::remove); // Удаление всех задач из истории
        subtaskList.values().stream()
                .filter(task -> task.getStartTime() != null)
                .forEach(prioritizedTasks::remove); // Удаление из списка по приоритету

        subtaskList.clear();

        epicList.values().stream().forEach(Epic::deleteAllSubtask); // Удаляем все подзадачи в эпиках и обновляем статусы
        epicList.values().stream().forEach(this::setStatusEpic);
    }

    // Получение подзадачи по ID
    @Override
    public Subtask getSubtaskByID(int id) {
        final Subtask subtask = subtaskList.get(id);

        if (subtask == null) {
            throw new NotFoundException("Сабтаск не найден");
        }

        historyManager.addHistory(subtask);
        return subtask;
    }

    // Обновление подзадачи
    @Override
    public void updateSubtask(Subtask subtask) {
        checkConflictTime(subtask);

        subtaskList.put(subtask.getId(), subtask);
        setStatusEpic(epicList.get(subtask.getParentId()));
        addInPrioritizedTasksTreeSet(subtask); // Добавляем в список по приоритету
    }

    // Удаление подзадачи
    @Override
    public void deleteSubtask(int id) {
        int parentEpicId = subtaskList.get(id).getParentId();
        Epic epic = epicList.get(parentEpicId);
        epic.deleteSubtask(id);
        setStatusEpic(epic);
        setTimeEpic(epic); // Обновление сроков эпика
        prioritizedTasks.remove(subtaskList.get(id)); // Удаление из списка по приоритету

        subtaskList.remove(id);
        historyManager.remove(id); // Удаление из истории
    }

    // Метод добавляет сабтаск в список. Необходим для восстановления задач из файла
    public void addTSubtaskInSubtaskList(Subtask subtask) {
        subtaskList.put(subtask.getId(), subtask);
        epicList.get(subtask.getParentId()).addSubtask(subtask.getId()); // Добавляем сабтаск в эпик
        addInPrioritizedTasksTreeSet(subtask); // Добавляем в список по приоритету
    }
}