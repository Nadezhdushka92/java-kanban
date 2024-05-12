package taskmanager.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Epic extends Task {
    private List<Integer> idSubTasks;

    private LocalDateTime endTime;

    public Epic(String name, String description, long duration, LocalDateTime time) {
        super(name, description, duration, time);
        this.type = TaskType.EPIC;
        idSubTasks = new ArrayList<>();
    }

    public Epic(String name, String description, Status status, long duration, LocalDateTime time) {
        super(name, description, status, duration, time);
        idSubTasks = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status, long duration, LocalDateTime time) {
        super(id, name, description, status, duration, time);
        idSubTasks = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status, ArrayList<Integer> idSubTask, long duration, LocalDateTime time) {
        super(id, name, description, status, duration, time);
        this.idSubTasks = idSubTask;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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