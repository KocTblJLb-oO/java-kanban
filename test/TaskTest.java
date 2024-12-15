import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.moysayt.steptraker.model.StatusOfTask;
import ru.moysayt.steptraker.model.Task;

class TaskTest {

    Task task1;
    Task task2;

    @BeforeEach
    void startTest() {
        task1 = new Task("", "testTask", StatusOfTask.NEW);
        task2 = new Task("", "testTask", StatusOfTask.NEW);
    }

    // проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    @Test
    void assertEqualsTaskById() {
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2, "Таски не равны");
    }
}