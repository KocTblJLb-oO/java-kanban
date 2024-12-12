package ru.moysayt.steptraker.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    Task subTask1;
    Task subTask2;

    @BeforeEach
    void startTest() {
        subTask1 = new Subtask(1, "test", "testTask", StatusOfTask.NEW);
        subTask2 = new Subtask(1, "test", "testTask", StatusOfTask.NEW);
    }

    // проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    void assertEqualsEpicById() {
        subTask1.setId(1);
        subTask2.setId(1);
        assertEquals(subTask1, subTask2, "Сабтаски не равны");
    }

    // проверьте, что объект Subtask нельзя сделать своим же эпиком;
    /*
    У меня Subtask хранит список id эпиков и добавить просто id возможно.
    */

}