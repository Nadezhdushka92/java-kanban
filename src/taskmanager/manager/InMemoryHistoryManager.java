package taskmanager.manager;

import taskmanager.tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> tasksHistoryList;

    public InMemoryHistoryManager() {
        tasksHistoryList = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            tasksHistoryList.add(task);
            //Храним историю в LinkedList, чтобы удаление первого элемента было быстрее
            if (tasksHistoryList.size() > 10) {
                tasksHistoryList.removeFirst();
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistoryList;
    }
}
