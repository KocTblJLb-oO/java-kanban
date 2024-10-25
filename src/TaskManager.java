import java.util.HashMap;

public class TaskManager {
    int taskID = 0;
    HashMap<Integer, Task> taskList = new HashMap<>();
    HashMap<Integer, Epic> epicList = new HashMap<>();
    HashMap<Integer, Subtask> subtaskList = new HashMap<>();

/*
------------------------------------------------ РАБОТА С ЗАДАЧАМИ
 */

    // Создание задачи
    public void createTask(Task t) {
        taskList.put(t.id, t);
    }

    // Показать все задачи
    public void showAllTask() {
        if (taskList.isEmpty()) {
            System.out.println("Задач нет");
        } else {
            for (Task t : taskList.values()) {
                System.out.println(t);
            }
        }
    }

    // Удаление всех задач
    public void clearTaskList() {
        taskList.clear();
    }

    // Получение задачи по ID
    public Task getTaskByID(int id) {
        return taskList.get(id);
    }

    // Обновление задачи
    public void updateTask(Task t) {
        taskList.put(t.id, t);
    }

    // Удаление задачи
    public void deleteTask(int id) {
        if (!taskList.containsKey(id)) {
            System.out.println("Задачи с ID " + id + " нет.");
        } else {
            taskList.remove(id);
        }
    }

    /*
------------------------------------------------ РАБОТА С ЭПИКАМИ
 */
// Создание эпика
    public void createEpic(Epic e) {
        epicList.put(e.id, e);
    }

    // Показать все задачи
    public void showAllEpic() {
        if (epicList.isEmpty()) {
            System.out.println("Эпиков нет");
        } else {
            for (Epic e : epicList.values()) {
                System.out.println(e);
            }
        }
    }

    // Удаление всех эпиков
    public void clearEpicList() {
        epicList.clear();
    }

    // Получение эпика по ID
    public Epic getEpicByID(int id) {
        return epicList.get(id);
    }

    // Обновление задачи
    public void updateEpic(Epic e) {
        epicList.put(e.id, e);
        e.setStatus(this);
    }

    // Удаление эпика
    public void deleteEpic(int id) {
        if (!epicList.containsKey(id)) {
            System.out.println("Эпика с ID " + id + " нет.");
        } else {
            // Удаление подзадач этого эпика
            for (int subID : epicList.get(id).mySubtaskID) {
                subtaskList.remove(subID);
            }
            epicList.remove(id);
        }
    }

    /*
------------------------------------------------ РАБОТА С ПОДЗАДАЧАМИ
 */
    // Создание подзадачи
    public void createSubtask(int epicID, Subtask s) {
        subtaskList.put(s.id, s);
        epicList.get(epicID).mySubtaskID.add(s.id); // Добавляем id подзадачи в эпик
    }

    // Показать все подзадачи
    public void showAllSubtask() {
        if (subtaskList.isEmpty()) {
            System.out.println("Подзадач нет.");
        } else {
            for (Subtask s : subtaskList.values()) {
                System.out.println(s);
            }
        }
    }

    // Удаление всех подзадач
    public void clearSubtaskList() {
        subtaskList.clear();
        // Обновление статуса всех эпиков
        for (Epic e : epicList.values()) {
            e.mySubtaskID.clear();
            e.setStatus(this);
        }
    }

    // Получение подзадачи по ID
    public Subtask getSubtaskByID(int id) {
        return subtaskList.get(id);
    }

    // Обновление подзадачи
    public void updateSubtask(Subtask s) {
        subtaskList.put(s.id, s);
        epicList.get(s.parentID).setStatus(this);
    }

    // Удаление подзадачи
    public void deleteSubtask(int id) {
        if (!subtaskList.containsKey(id)) {
            System.out.println("Подзадачи с ID " + id + " нет.");
        } else {
            epicList.get(subtaskList.get(id).parentID).mySubtaskID.
                    remove(epicList.get(subtaskList.get(id).parentID).mySubtaskID.indexOf(id));
            epicList.remove(id);
        }


    }
}
