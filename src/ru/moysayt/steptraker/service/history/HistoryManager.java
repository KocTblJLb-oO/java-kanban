package ru.moysayt.steptraker.service.history;

import ru.moysayt.steptraker.model.Task;

import java.util.ArrayList;

public interface HistoryManager {

     /*
------------------------------------------------ История задач
 */

    // Сохранение истории простмотра
    void addHistory(Task task);

    // Получение истории просмотра
    ArrayList<Task> getHistory();


}