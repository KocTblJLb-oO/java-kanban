package ru.moysayt.steptraker.service.history;
import ru.moysayt.steptraker.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
   private final List<Task> taskHistory = new LinkedList<>();
   private static final int MAX_SIZE_HISTORY = 10;

    /*
------------------------------------------------ История задач
 */

    // Сохранение истории простмотра
    @Override
    public void addHistory(Task task){
        taskHistory.add(task);
        if (taskHistory.size() > MAX_SIZE_HISTORY){
            taskHistory.remove(0);
        }
    };

    // Получение истории просмотра
    @Override
    public List<Task> getHistory(){
        return List.copyOf(taskHistory);
    };}
