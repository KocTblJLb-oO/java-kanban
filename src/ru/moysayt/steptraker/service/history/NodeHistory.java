package ru.moysayt.steptraker.service.history;

import ru.moysayt.steptraker.model.Task;

public class NodeHistory<T extends Task> {
    public T task;
    public NodeHistory<T> next;
    public NodeHistory<T> prev;

    public NodeHistory(NodeHistory<T> prev, T task, NodeHistory<T> next){
        this.prev = prev;
        this.task = task;
        this.next = next;
    }
}
