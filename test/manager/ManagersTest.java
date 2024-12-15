package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.moysayt.steptraker.manager.Managers;
import ru.moysayt.steptraker.service.TaskManager;

class ManagersTest {

    // убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    @Test
    void createInMemoryTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        Assertions.assertNotNull(taskManager);
    }

}