package service.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.InMemoryTaskManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryTaskManager inMemoryTaskManager;
    Task task1;
    Task task2;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void startTest() {
        inMemoryTaskManager = new InMemoryTaskManager();
        task1 = new Task("", "testTask", StatusOfTask.NEW);
        task2 = new Task("", "testTask", StatusOfTask.NEW);
        epic = new Epic("", "testEpic", StatusOfTask.NEW);
        subtask = new Subtask(1, "", "testSubtask", StatusOfTask.NEW);

        task1.setId(1);
        epic.setId(2);
        task2.setId(3);
        subtask.setId(4);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createSubtask(2, subtask);

        inMemoryTaskManager.getTaskByID(task1.getId());
        inMemoryTaskManager.getEpicByID(epic.getId());
        inMemoryTaskManager.getTaskByID(task2.getId());
        inMemoryTaskManager.getSubtaskByID(subtask.getId());
    }

    @Test
    void addHistory() {

        assertEquals(4, inMemoryTaskManager.historyManager.getSize(),
                "Длинна истории некорректна");
    }

    @Test
    void getHistory() {
        List<Task> historyTaskListSample = new ArrayList<>();
        historyTaskListSample.add(task1);
        historyTaskListSample.add(epic);
        historyTaskListSample.add(task2);
        historyTaskListSample.add(subtask);

        List<Task> historyTaskList = inMemoryTaskManager.historyManager.getHistory();

        assertEquals(historyTaskListSample, historyTaskList,
                "История не совпадает с эталонной");
    }

    @Test
    void remove() {
        inMemoryTaskManager.historyManager.remove(1);
        inMemoryTaskManager.historyManager.remove(3);

        List<Task> historyTaskList = inMemoryTaskManager.historyManager.getHistory();

        assertEquals(2, historyTaskList.size(), "Просмотры не удаляются");
    }
}