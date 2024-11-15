package ru.moysayt.steptraker;

import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.InMemoryTaskManager;
import ru.moysayt.steptraker.service.history.InMemoryHistoryManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        // Создаём две задачи
        Task t1 = new Task("Заголовок 1", "Описание задачи 1", StatusOfTask.NEW);
        Task t2 = new Task("Заголовок 2", "Описание задачи 2", StatusOfTask.NEW);
        inMemoryTaskManager.createTask(t1);
        inMemoryTaskManager.createTask(t2);

        // Создаём эпик с двумя подзадачами
        Epic e1 = new Epic("Эпик 1", "Описание эпика 1", StatusOfTask.NEW);
        inMemoryTaskManager.createEpic(e1);
        Subtask s1 = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", StatusOfTask.NEW);
        Subtask s2 = new Subtask(3, "Подзадача 2", "Описание подзадачи 2", StatusOfTask.NEW);
        inMemoryTaskManager.createSubtask(3, s1);
        inMemoryTaskManager.createSubtask(3, s2);

        // Создаём эпик с одной подзадачей
        Epic e2 = new Epic("Эпик 2. С одной подзадачей", "Описание эпика 2. " + "С одной подзадачей"
                , StatusOfTask.NEW);
        inMemoryTaskManager.createEpic(e2);
        Subtask s3 = new Subtask(6, "Подзадача 3. С эпиком с 1 зад.", "Описание подзадачи 3. С эпиком с 1 зад."
                , StatusOfTask.NEW);
        inMemoryTaskManager.createSubtask(6, s3);

        showAllTask(inMemoryTaskManager);
        showAllEpic(inMemoryTaskManager);
        showAllSubtask(inMemoryTaskManager);

        // Меняем статус задачи и подзадачи
        System.out.println("------------------------------------------------\n" +
                "ИЗМЕНЕНИЕ СТАТУСА\n" +
                "------------------------------------------------");
        Task t1newStatus = inMemoryTaskManager.getTaskByID(1);
        t1newStatus.setStatus(StatusOfTask.IN_PROGRESS);
        inMemoryTaskManager.updateTask(t1newStatus);
        Subtask s3NewStatus = inMemoryTaskManager.getSubtaskByID(7);
        s3NewStatus.setStatus(StatusOfTask.DONE);
        inMemoryTaskManager.updateSubtask(s3NewStatus);
        System.out.println(inMemoryTaskManager.getTaskByID(1));
        // Печатаем вместе с родительским эпиком
        System.out.println(inMemoryTaskManager.getSubtaskByID(7));
        System.out.println(inMemoryTaskManager.getEpicByID(6));

        // Удалим одну задачу и эпик с двумя подзадачами
        System.out.println("------------------------------------------------\n" +
                "УДАЛЕНИЕ\n" +
                "------------------------------------------------");
        inMemoryTaskManager.deleteTask(2);
        inMemoryTaskManager.deleteEpic(3);

        showAllTask(inMemoryTaskManager);
        showAllEpic(inMemoryTaskManager);
        showAllSubtask(inMemoryTaskManager);

        System.out.println("------------------------------------------------\n" +
                "ИСТОРИЯ\n" +
                "------------------------------------------------");
        showHistory((InMemoryHistoryManager) inMemoryTaskManager.historyManager);
    }

    public static void showAllTask(InMemoryTaskManager inMemoryTaskManager) {
        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println(task);
        }
    }

    public static void showAllEpic(InMemoryTaskManager inMemoryTaskManager) {
        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println(epic);
        }
    }

    public static void showAllSubtask(InMemoryTaskManager inMemoryTaskManager) {
        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println(subtask);
        }
    }

    public static void showHistory(InMemoryHistoryManager historyManager) {
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }
    }
}
