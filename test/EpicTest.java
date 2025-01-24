import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Subtask;
import ru.moysayt.steptraker.service.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    Epic epic1;
    Epic epic2;
    Subtask sub1;
    Subtask sub2;
    Subtask sub3;
    InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    void startTest() {
        epic1 = new Epic("", "testTask", StatusOfTask.NEW);
        epic2 = new Epic("", "testTask", StatusOfTask.NEW);
        sub1 = new Subtask(1, "qqqq", "dddd", StatusOfTask.NEW);
        sub2 = new Subtask(1, "qqqq", "dddd", StatusOfTask.NEW);
        sub3 = new Subtask(1, "qqqq", "dddd", StatusOfTask.IN_PROGRESS);
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    // проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    void assertEqualsEpicById() {
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1, epic2, "Эпики не равны");
    }

    // проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;
    /*
    У меня Epic хранит список id сабтасков и добавить просто id возможно.
    */

    // ------------------------------------------------

    /*Для расчёта статуса Epic. Граничные условия:
    a. Все подзадачи со статусом NEW.
    b. Все подзадачи со статусом DONE.
    c. Подзадачи со статусами NEW и DONE.
    d. Подзадачи со статусом IN_PROGRESS. */

    @Test
    void assertEqualsStatusNew() {
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createSubtask(1, sub1);
        inMemoryTaskManager.createSubtask(1, sub2);

        assertEquals(StatusOfTask.NEW, inMemoryTaskManager.getEpicByID(1).getStatus());
    }

    @Test
    void assertEqualsStatusInProgress() {
        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.createSubtask(1, sub3);

        assertEquals(StatusOfTask.IN_PROGRESS, inMemoryTaskManager.getEpicByID(1).getStatus(), "Статус Эпика не обновился");
    }

}