package taskmanager.manager;
import java.io.File;
import java.nio.file.Path;

public class Managers { //static?
    protected Managers() {

    }
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(); //getDefaultHistoryManager()
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
