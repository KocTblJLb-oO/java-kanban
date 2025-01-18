package ru.moysayt.steptraker.manager;

import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.FileBackedTaskManager;
import ru.moysayt.steptraker.service.InMemoryTaskManager;
import ru.moysayt.steptraker.service.TaskManager;
import ru.moysayt.steptraker.service.history.HistoryManager;
import ru.moysayt.steptraker.service.history.InMemoryHistoryManager;

import java.io.File;
import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() throws IOException {
        return new FileBackedTaskManager(File.createTempFile("fileForTask-", ".csv"));
    }

    public static HistoryManager<Task> getDefaultHistory() {
        return new InMemoryHistoryManager<>();
    }
}
