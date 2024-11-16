package ru.moysayt.steptraker.service.history;

import ru.moysayt.steptraker.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {

     /*
------------------------------------------------ История задач
 */

    // Сохранение истории простмотра
    void addHistory(Task task);

    // Получение истории просмотра
    List<Task> getHistory();


}