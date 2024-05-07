package taskmanager.manager;


import java.io.File;

public class Managers { //static?
    private Managers() {

    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(); //getDefaultHistoryManager()
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultFileBackedTaskManager() {
        return new FileBackedTaskManager(new File("./java-kanban/test/resources/saveTaskManagerFile.csv"));
    }
}
