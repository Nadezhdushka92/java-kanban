package test;

import taskmanager.manager.FileBackedTaskManager;
import taskmanager.manager.InMemoryTaskManager;

import java.io.File;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createTaskManager () {
        return manager = new InMemoryTaskManager();
    }

    @Override
    protected InMemoryTaskManager createLoadFileTaskManager () {
        return null;
    }


}