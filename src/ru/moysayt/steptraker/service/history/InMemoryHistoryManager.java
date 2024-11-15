package ru.moysayt.steptraker.service.history;
import ru.moysayt.steptraker.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    ArrayList<Task> taskHistory = new ArrayList<>();

    /*
------------------------------------------------ История задач
 */

    // Сохранение истории простмотра
    @Override
    public void addHistory(Task task){
        taskHistory.add(task);
        if (taskHistory.size() > 10){
            taskHistory.remove(0);
        }
    };

    // Получение истории просмотра
    @Override
    public ArrayList<Task> getHistory(){
        return taskHistory;
    };}
