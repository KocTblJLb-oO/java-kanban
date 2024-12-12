package ru.moysayt.steptraker.service.history;

import ru.moysayt.steptraker.model.Task;

import java.util.*;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {
    private final HashMap<Integer, NodeHistory<T>> historyMap = new HashMap<>();
    private NodeHistory<T> head;
    private NodeHistory<T> tail;
    private int size;

    /*
------------------------------------------------ История задач
 */

    // Сохранение истории просмотра
    @Override
    public void addHistory(T task) {
        if (historyMap.containsKey(task.getId())) {
            removeNode(task.getId());
            historyMap.remove(task.getId());
        }
        linkLast(task);
        size++;
    }

    // Получение истории просмотра
    @Override
    public List<Task> getHistory() {
        List<Task> listHistory = new ArrayList<>();
        listHistory = getTasks();
        return listHistory;
    }

    // Удаление из истории
    @Override
    public void remove(int id) {
        if (!historyMap.containsKey(id)) {
            return;
        }
        removeNode(id);
        historyMap.remove(id);
        size--;
    }

    @Override
    public int getSize() {
        return size;
    }

    /*
    ------------------------------------------------ Методы для работы с NodeTask
     */

    private void linkLast(T task) {
        final NodeHistory<T> oldTail = tail;
        final NodeHistory<T> newTail = new NodeHistory<>(tail, task, null);
        tail = newTail;

        if (oldTail == null) {
            head = newTail;
        } else {
            oldTail.next = tail;
        }
        historyMap.put(task.getId(), tail);
    }

    private List<Task> getTasks(){
        List<Task> listHistory = new ArrayList<>();
        NodeHistory<T> node = head;
        while (!(node == null)) { // Собираем элементы, пока не найдём хвост
            listHistory.add(node.task);
            node = node.next;
        }
        return listHistory;
    }

    private void removeNode(int id){
        NodeHistory<T> node = historyMap.get(id);
        NodeHistory<T> prev = node.prev;
        NodeHistory<T> next = node.next;

        if (prev == null) {
            next.prev = null; // Если у нас голова, то удаляем голову у следующего
            head = next; // И меняем голову списка
        } else if (next == null) {
            prev.next = null; // Если у нас хвост, то удаляем хвост у предыдущего
            tail = prev; // И меняем хвост списка
        } else {
            prev.next = next; // У предыдущего меняем хвост
            next.prev = prev; // , у следующего голову.
        }
    }
}
