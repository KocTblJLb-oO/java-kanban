import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.moysayt.steptraker.model.Epic;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    Task epic1;
    Task epic2;

    @BeforeEach
    void startTest() {
        epic1 = new Epic("", "testTask", StatusOfTask.NEW);
        epic2 = new Epic("", "testTask", StatusOfTask.NEW);
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
}