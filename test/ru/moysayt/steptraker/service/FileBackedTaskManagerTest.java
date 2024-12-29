package ru.moysayt.steptraker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    FileBackedTaskManager fileBackedTaskManager;
    File file;
    Task task1;
    Task task2;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void startTest() throws IOException {
        file = File.createTempFile("fileForTask-", ".csv");
        fileBackedTaskManager = new FileBackedTaskManager(file);
        task1 = new Task("ааа", "testTask", StatusOfTask.NEW);
        task2 = new Task("ааа", "testTask", StatusOfTask.NEW);
        epic = new Epic("ааа", "testEpic", StatusOfTask.NEW);
        subtask = new Subtask(3, "fff", "testSubtask", StatusOfTask.NEW);
    }

    @Test
    void loadFromFile() {
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createTask(task2);
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.createSubtask(3, subtask);
        int countTask = fileBackedTaskManager.getTasks().size() + fileBackedTaskManager.getEpics().size()
                + fileBackedTaskManager.getSubtasks().size();

        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file);
        int countInFile = fileBackedTaskManager2.getTasks().size() + fileBackedTaskManager2.getEpics().size()
                + fileBackedTaskManager2.getSubtasks().size();

        assertEquals(countTask, countInFile, "Восстановление из файла не корректно. Кол-во задач не совпадает");

    }
}