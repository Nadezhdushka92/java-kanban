package taskmanager.manager;

import taskmanager.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> tasksHistoryList;

    public InMemoryHistoryManager() {
        tasksHistoryList = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            tasksHistoryList.add(task);

            if (tasksHistoryList.size() > 10) {
                tasksHistoryList.remove(0);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistoryList;
    }
}
