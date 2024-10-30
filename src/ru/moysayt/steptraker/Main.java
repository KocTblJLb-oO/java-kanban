package ru.moysayt.steptraker;

import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        // Создаём две задачи
        Task t1 = new Task("Заголовок 1", "Описание задачи 1", StatusOfTask.NEW);
        Task t2 = new Task("Заголовок 2", "Описание задачи 2", StatusOfTask.NEW);
        taskManager.createTask(t1);
        taskManager.createTask(t2);

        // Создаём эпик с двумя подзадачами
        Epic e1 = new Epic("Эпик 1", "Описание эпика 1", StatusOfTask.NEW);
        taskManager.createEpic(e1);
        Subtask s1 = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", StatusOfTask.NEW);
        Subtask s2 = new Subtask(3, "Подзадача 2", "Описание подзадачи 2", StatusOfTask.NEW);
        taskManager.createSubtask(3, s1);
        taskManager.createSubtask(3, s2);

        // Создаём эпик с одной подзадачей
        Epic e2 = new Epic("Эпик 2. С одной подзадачей", "Описание эпика 2. " + "С одной подзадачей"
                , StatusOfTask.NEW);
        taskManager.createEpic(e2);
        Subtask s3 = new Subtask(6, "Подзадача 3. С эпиком с 1 зад.", "Описание подзадачи 3. С эпиком с 1 зад."
                , StatusOfTask.NEW);
        taskManager.createSubtask(6, s3);

        showAllTask(taskManager);
        showAllEpic(taskManager);
        showAllSubtask(taskManager);

        // Меняем статус задачи и подзадачи
        System.out.println("------------------------------------------------\n" +
                "ИЗМЕНЕНИЕ СТАТУСА\n" +
                "------------------------------------------------");
        Task t1newStatus = taskManager.getTaskByID(1);
        t1newStatus.setStatus(StatusOfTask.IN_PROGRESS);
        taskManager.updateTask(t1newStatus);
        Subtask s3NewStatus = taskManager.getSubtaskByID(7);
        s3NewStatus.setStatus(StatusOfTask.DONE);
        taskManager.updateSubtask(s3NewStatus);
        System.out.println(taskManager.getTaskByID(1));
        // Печатаем вместе с родительским эпиком
        System.out.println(taskManager.getSubtaskByID(7));
        System.out.println(taskManager.getEpicByID(6));

        // Удалим одну задачу и эпик с двумя подзадачами
        System.out.println("------------------------------------------------\n" +
                "УДАЛЕНИЕ\n" +
                "------------------------------------------------");
        taskManager.deleteTask(2);
        taskManager.deleteEpic(3);

        showAllTask(taskManager);
        showAllEpic(taskManager);
        showAllSubtask(taskManager);
    }

    public static void showAllTask(TaskManager taskManager) {
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
    }

    public static void showAllEpic(TaskManager taskManager) {
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
    }

    public static void showAllSubtask(TaskManager taskManager) {
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
    }
}
