package ru.moysayt.steptraker.service.history;

import ru.moysayt.steptraker.model.Task;

import java.util.List;

public interface HistoryManager<T extends Task> {

     /*
------------------------------------------------ История задач
 */

    // Сохранение истории просмотра
    void addHistory(T task);

    // Получение истории просмотра
    List<Task> getHistory();

    //Удаление истории из просмотра
    void remove(int id);

    int getSize();
}