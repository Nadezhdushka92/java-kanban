package taskmanager.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Epic extends Task {
    private List<Integer> idSubTasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        idSubTasks = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
        idSubTasks = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status, ArrayList<Integer> idSubTask) {
        super(id, name, description, status);
        this.idSubTasks = idSubTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(idSubTasks, epic.idSubTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubTasks);
    }

    public List<Integer> getIdSubTask() {
        return new ArrayList<>(idSubTasks);
    }

    public void setIdSubTask(List<Integer> idSubTask) {
        this.idSubTasks = idSubTask;
    }

    public void addNewSubTask(int id) {
        idSubTasks.add(id);
    }

    public void delSubTask(int id) {
        idSubTasks.remove(id);
    }

    public void delAllSubTasks() {
        idSubTasks.clear();
    }

}
