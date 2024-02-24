package taskmanager.typetask;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subTasks = new ArrayList<>();
    }

    public Epic(String name, String description, Status status, ArrayList<SubTask> subTasks) {
        super(name, description, status);
        subTasks = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status, ArrayList<SubTask> subTasks) {
        super(id, name, description, status);
        subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }
}
