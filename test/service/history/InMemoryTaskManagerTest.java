package service.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.service.InMemoryTaskManager;
import ru.moysayt.steptraker.service.directory.ManagerSaveException;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryTaskManagerTest {
    InMemoryTaskManager inMemoryTaskManager;
    Task task1;
    Task task2;
    Task task3;
    Task task4;
    Epic epic;
    Subtask subtask;
    LocalDateTime localDateTime;
    Duration duration;

    @BeforeEach
    void startTest() {
        inMemoryTaskManager = new InMemoryTaskManager();
        localDateTime = LocalDateTime.now();
        duration = Duration.ofMinutes(10);
        task1 = new Task("", "testTask", StatusOfTask.NEW);
        task2 = new Task("", "testTask", StatusOfTask.NEW);
        task3 = new Task("", "testTask", StatusOfTask.NEW, localDateTime, duration);
        task4 = new Task("", "testTask", StatusOfTask.NEW, localDateTime.plus(Duration.ofMinutes(5)), duration);
        epic = new Epic("", "testEpic", StatusOfTask.NEW);
        subtask = new Subtask(1, "", "testSubtask", StatusOfTask.NEW);
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
        task2.setId(inMemoryTaskManager.getId());

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);

        assertEquals(2, inMemoryTaskManager.getTasks().size(), "Объекты конфликтуют");
    }

    // Тест пересечения задач по времени
    @Test
    void conflictTime() {
        inMemoryTaskManager.createTask(task3);

        assertThrows(ManagerSaveException.class, () -> inMemoryTaskManager.createTask(task4)
                , "Удалось добавить конфликтующие задачи");
    }
}