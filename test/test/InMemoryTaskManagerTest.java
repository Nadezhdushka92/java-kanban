package test;

import org.junit.jupiter.api.BeforeEach;
import taskmanager.manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void init() {
        createTaskManager();
    }
    @Override
    public InMemoryTaskManager createTaskManager() {
        manager = new InMemoryTaskManager();
        return manager;
    }


}