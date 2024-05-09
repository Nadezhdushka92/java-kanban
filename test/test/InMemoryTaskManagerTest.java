package test;

import taskmanager.manager.FileBackedTaskManager;
import taskmanager.manager.InMemoryTaskManager;

import java.io.File;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    File fileSavedHistory = new File("C:\\Users\\12345\\IdeaProjects\\java-kanban[Sprint7]\\java-kanban\\test\\resources\\saveTaskManagerTest.csv");//src/java-kanban/test/resources/saveTaskManagerTest.csv

    @Override
    public InMemoryTaskManager createTaskManager () {
        return manager = new InMemoryTaskManager();
    }

    @Override
    protected InMemoryTaskManager createLoadFileTaskManager () {
        return fileManager = FileBackedTaskManager.loadFromFile(fileSavedHistory);
    }


}