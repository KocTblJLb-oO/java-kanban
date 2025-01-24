package ru.moysayt.steptraker.service;

import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    /*
------------------------------------------------ МЕТОДЫ ТАСК МЕНЕДЖЕРА
*/
    // Возвращает историю просмотров
    List<Task> getHistory();
/*
------------------------------------------------ РАБОТА С ЗАДАЧАМИ
 */

    // Создание задачи
    void createTask(Task task);

    // Получение всех задач
    ArrayList<Task> getTasks();

    // Удаление всех задач
    void clearTaskList();

    // Получение задачи по ID
    Task getTaskByID(int id);

    // Обновление задачи
    void updateTask(Task task);

    // Удаление задачи
    void deleteTask(int id);

    /*
------------------------------------------------ РАБОТА С ЭПИКАМИ
 */

    // Создание эпика
    void createEpic(Epic epic);

    // Получение всех эпиков
    ArrayList<Epic> getEpics();

    // Удаление всех эпиков
    void clearEpicList();

    // Получение эпика по ID
    Epic getEpicByID(int id);

    // Обновление эпика
    void updateEpic(Epic epic);

    // Удаление эпика
    void deleteEpic(int id);

    // Получение подзадач эпика
    ArrayList<Subtask> getEpicSubtask(int epicId);

    /*
------------------------------------------------ РАБОТА С ПОДЗАДАЧАМИ
 */

    // Создание подзадачи
    void createSubtask(int epicId, Subtask subtask);

    // Получение всех подзадач
    ArrayList<Subtask> getSubtasks();

    // Удаление всех подзадач
    void clearSubtaskList();

    // Получение подзадачи по ID
    Subtask getSubtaskByID(int id);

    // Обновление подзадачи
    void updateSubtask(Subtask subtask);

    // Удаление подзадачи
    void deleteSubtask(int id);

}
