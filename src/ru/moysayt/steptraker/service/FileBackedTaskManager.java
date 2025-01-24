package ru.moysayt.steptraker.service;

import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.model.Task;
import ru.moysayt.steptraker.service.directory.ManagerSaveException;
import ru.moysayt.steptraker.service.directory.TypeOfTask;

import java.io.*;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File fileForTasks;

    public FileBackedTaskManager(File file) {
        this.fileForTasks = file;
    }

    /*
------------------------------------------------ МЕТОДЫ ФАЙЛ-МЕНЕДЖЕРА
*/

    // Задача в строку
    private String taskToString(Task task) {
        TypeOfTask typeOfTask = task.getTypeOfTask();

        String subtaskParentId = "";
        if (typeOfTask == TypeOfTask.SUBTASK) {
            Subtask subtask = (Subtask) task;
            subtaskParentId = Integer.toString(subtask.getParentId());
        }

        return task.getId() + ";" + typeOfTask + ";" + task.getTitle() + ";" + task.getStatus() + ";"
                + task.getText() + ";" + subtaskParentId + ";" + task.getStartTime() + ";" + task.getDuration() + ";"
                + task.getEndTime();
    }

    // Сохраняем текущее состояние файл-менеджера в файл. Каждый раз сохраняется всё, перезаписывая файл
    private void save() {
        try (FileWriter wr = new FileWriter(fileForTasks, Charset.forName("Windows-1251"));
             BufferedWriter bw = new BufferedWriter(wr)) {
            bw.write("id;type;name;status;description;epic;startTime;duration;endTime\n"); // Добавляем в файл шапку

            final ArrayList<Task> allTaskList = this.getTasks();
            final ArrayList<Epic> allEpicList = this.getEpics();
            final ArrayList<Subtask> allSubtaskList = this.getSubtasks();

            for (Task task : allTaskList) { // Записываем в файл все таски
                bw.write(taskToString(task));
                bw.newLine();
            }
            for (Epic epic : allEpicList) { // Записываем в файл все эпики
                bw.write(taskToString(epic));
                bw.newLine();
            }
            for (Subtask subtask : allSubtaskList) { // Записываем в файл все сабтаски
                bw.write(taskToString(subtask));
                bw.newLine();
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла");
        }
    }

    // Создание задачи из строки файла
    private static Task fromString(String value) {
        String[] values = value.split(";");
        Task task = null;

        int taskId = Integer.parseInt(values[0]);
        String typeOfTask = values[1];
        String name = values[2];
        String status = values[3];
        String descriptionTask = values[4];
        String startTimeString = values[6];
        String durationString = values[7];

        if (TypeOfTask.valueOf(typeOfTask).equals(TypeOfTask.TASK)) {
            if (startTimeString.equals("null")) {
                task = new Task(name, descriptionTask, StatusOfTask.valueOf(status));
            } else {
                LocalDateTime startTime = LocalDateTime.parse(startTimeString);
                Duration duration = Duration.parse(durationString);
                task = new Task(name, descriptionTask, StatusOfTask.valueOf(status), startTime, duration);
            }
            task.setId(taskId);
        } else if (TypeOfTask.valueOf(typeOfTask).equals(TypeOfTask.EPIC)) {
            if (startTimeString.equals("null")) {
                task = new Epic(name, descriptionTask, StatusOfTask.valueOf(status));
            } else {
                LocalDateTime startTime = LocalDateTime.parse(startTimeString);
                Duration duration = Duration.parse(durationString);
                task = new Epic(name, descriptionTask, StatusOfTask.valueOf(status), startTime, duration);
            }
            task.setId(taskId);
        } else if (TypeOfTask.valueOf(typeOfTask).equals(TypeOfTask.SUBTASK)) {
            int epicId = Integer.parseInt(values[5]);
            if (startTimeString.equals("null")) {
                task = new Subtask(epicId, name, descriptionTask, StatusOfTask.valueOf(status));
            } else {
                LocalDateTime startTime = LocalDateTime.parse(startTimeString);
                Duration duration = Duration.parse(durationString);
                task = new Subtask(epicId, name, descriptionTask, StatusOfTask.valueOf(status), startTime, duration);
            }
            task.setId(taskId);
        }

        return task;
    }

    // Восстановление задач из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        try (FileReader fr = new FileReader(file, Charset.forName("Windows-1251"));
             BufferedReader br = new BufferedReader(fr)) {
            br.readLine(); // Пропускаем первую строку файла
            while (br.ready()) {
                String line = br.readLine();
                Task task = fromString(line);

                switch (task.getClass().getName()) {
                    case "ru.moysayt.steptraker.model.Task" -> fileBackedTaskManager.addTaskInTaskList(task);
                    case "ru.moysayt.steptraker.model.Epic" -> fileBackedTaskManager.addEpicInEpicList((Epic) task);
                    case "ru.moysayt.steptraker.model.Subtask" -> fileBackedTaskManager
                            .addTSubtaskInSubtaskList((Subtask) task);
                }
            }

            setMaxId(fileBackedTaskManager); // Устанавливаем счётчик id

        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл: " + e);
        }
        return fileBackedTaskManager;
    }

    // Устанавливаем счётчик id задач по максимальному значению из файла, чтобы новые задачи получали корректный id
    private static void setMaxId(FileBackedTaskManager fm) {
        int maxId = 0;

        for (Task task : fm.getTasks()) {
            if (maxId < task.getId()) {
                maxId = task.getId();
            }
        }
        for (Epic epic : fm.getEpics()) {
            if (maxId < epic.getId()) {
                maxId = epic.getId();
            }
        }
        for (Subtask subtask : fm.getSubtasks()) {
            if (maxId < subtask.getId()) {
                maxId = subtask.getId();
            }
        }

        fm.setStartId(maxId);
    }

    /*
------------------------------------------------ Переопределённые методы InMemoryTaskManager.java
*/

//------------------------------------------------ Задачи

    // Создание задачи
    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    // Удаление всех задач
    @Override
    public void clearTaskList() {
        super.clearTaskList();
        save();
    }

    // Обновление задачи
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    // Удаление задачи
    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

//------------------------------------------------ Эпики

    // Создание эпика
    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    // Удаление всех эпиков
    @Override
    public void clearEpicList() {
        super.clearEpicList();
        save();
    }

    // Обновление эпика
    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    // Удаление эпика
    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

//------------------------------------------------ Сабтаски

    // Создание подзадачи
    @Override
    public void createSubtask(int epicId, Subtask subtask) {
        super.createSubtask(epicId, subtask);
        save();
    }

    // Удаление всех подзадач
    @Override
    public void clearSubtaskList() {
        super.clearSubtaskList();
        save();
    }

    // Обновление подзадачи
    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    // Удаление подзадачи
    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }
}
