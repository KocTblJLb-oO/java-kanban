package ru.moysayt.steptraker.manager;

import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.InMemoryTaskManager;
import ru.moysayt.steptraker.service.TaskManager;
import ru.moysayt.steptraker.service.history.HistoryManager;
import ru.moysayt.steptraker.service.history.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager<Task> getDefaultHistory() {
        return new InMemoryHistoryManager<>();
    }
}
