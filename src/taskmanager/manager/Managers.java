package taskmanager.manager;

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
