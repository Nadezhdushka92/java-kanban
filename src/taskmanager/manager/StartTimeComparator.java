package taskmanager.manager;

import taskmanager.tasks.Task;

import java.util.Comparator;

public class StartTimeComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {

        if (o1.getStartTime() != null && o2.getStartTime() != null) {
            return o1.getStartTime().compareTo(o2.getStartTime());

        } else if (o1.getStartTime() != null) {
            return -1;

        } else if (o2.getStartTime() != null) {
            return 1;

        } else if (o1.getId() == o2.getId()) {
            return 0;

        } else {
            return 1;
        }
    }
}
