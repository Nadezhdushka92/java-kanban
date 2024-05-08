package test;

import taskmanager.manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    InMemoryTaskManager createTaskManager () {
        manager = new InMemoryTaskManager();
        return null;
    }


}