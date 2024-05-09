package test;

import taskmanager.manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createTaskManager () {
        return manager = new InMemoryTaskManager();
    }

    @Override
    protected InMemoryTaskManager createFileTaskManager () {
        return null;
    }


}