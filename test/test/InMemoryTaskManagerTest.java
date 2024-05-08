package test;

import org.junit.jupiter.api.BeforeEach;
import taskmanager.manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void createTaskManager() {
        manager = new InMemoryTaskManager();
    }


}