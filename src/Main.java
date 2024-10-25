public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        // Создаём две задачи
        taskManager.taskID++;
        Task t1 = new Task(taskManager.taskID, "Заголовок 1", "Описание задачи 1", StatusOfTask.NEW);
        taskManager.taskID++;
        Task t2 = new Task(taskManager.taskID, "Заголовок 2", "Описание задачи 2",StatusOfTask.NEW);
        taskManager.createTask(t1);
        taskManager.createTask(t2);

        // Создаём эпик с двумя подзадачами
        taskManager.taskID++;
        Epic e1 = new Epic(taskManager.taskID, "Эпик 1", "Описание эпика 1", StatusOfTask.NEW);
        taskManager.createEpic(e1);
        taskManager.taskID++;
        Subtask s1 = new Subtask(e1.id, taskManager.taskID, "Подзадача 1", "Описание подзадачи 1"
                , StatusOfTask.NEW);
        taskManager.taskID++;
        Subtask s2 = new Subtask(e1.id, taskManager.taskID, "Подзадача 2", "Описание подзадачи 2"
                , StatusOfTask.NEW);
        taskManager.createSubtask(e1.id, s1);
        taskManager.createSubtask(e1.id, s2);

        // Создаём эпик с одной подзадачей
        taskManager.taskID++;
        Epic e2 = new Epic(taskManager.taskID, "Эпик 2. С одной подзадачей", "Описание эпика 2. " +
                "С одной подзадачей", StatusOfTask.NEW);
        taskManager.createEpic(e2);
        taskManager.taskID++;
        Subtask s3 = new Subtask(e2.id, taskManager.taskID, "Подзадача 3. С эпиком с 1 зад.",
                "Описание подзадачи 3. С эпиком с 1 зад.", StatusOfTask.NEW);
        taskManager.createSubtask(e2.id, s3);

        // Печатаем списки
        taskManager.showAllTask();
        taskManager.showAllEpic();
        taskManager.showAllSubtask();

        // Меняем статус задачи и подзадачи
        System.out.println("------------------------------------------------\n" +
                "ИЗМЕНЕНИЕ СТАТУСА\n" +
                "------------------------------------------------");
        t1.status = StatusOfTask.DONE;
        taskManager.updateTask(t1);
        s3.status = StatusOfTask.IN_PROGRESS;
        taskManager.updateSubtask(s3);
        System.out.println(taskManager.taskList.get(1));
        // Печатаем вместе с родительским эпиком
        System.out.println(taskManager.epicList.get(6));
        System.out.println(taskManager.subtaskList.get(7));

        // Удалим одну задачу и эпик с двумя подзадачами
        System.out.println("------------------------------------------------\n" +
                "УДАЛЕНИЕ\n" +
                "------------------------------------------------");
        taskManager.deleteTask(2);
        taskManager.deleteEpic(3);
        taskManager.showAllTask();
        taskManager.showAllEpic();
        taskManager.showAllSubtask();
    }
}
