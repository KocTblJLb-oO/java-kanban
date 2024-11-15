package ru.moysayt.steptraker.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.model.Subtask;

class InMemoryTaskManagerTest {
    InMemoryTaskManager inMemoryTaskManager;
    Task task1;
    Task task2;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void startTest() {
        inMemoryTaskManager = new InMemoryTaskManager();
        task1 = new Task("test", "testTask", StatusOfTask.NEW);
        task2 = new Task("test", "testTask", StatusOfTask.NEW);
        epic = new Epic("test", "testEpic", StatusOfTask.NEW);
        subtask = new Subtask(1, "test", "testSubtask", StatusOfTask.NEW);
    }

    // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    void addTaskAndFindById() {
        task1.setId(2);
        epic.setId(1);

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(2, subtask);

        assertEquals(task1, inMemoryTaskManager.getTaskByID(task1.getId()), "Таск не найден");
        assertEquals(epic, inMemoryTaskManager.getEpicByID(epic.getId()), "Эпик не найден");
        assertEquals(subtask, inMemoryTaskManager.getSubtaskByID(3), "Сабтаск не найден");
    }

    // проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    void createTaskByEqualsId() {
        task1.setId(1);
        task2.setId(inMemoryTaskManager.getNewTaskId());

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);

        assertEquals(2, inMemoryTaskManager.getTasks().size(), "Объекты конфликтуют");
    }
}