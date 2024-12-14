package taskmanager.tasks;

import java.time.LocalDateTime;
import java.util.Objects;


public class SubTask extends Task {
    private int idEpic;
    private LocalDateTime endTime;

//    @Override
//    public LocalDateTime getEndTime() {
//        return endTime;
//    }


    public SubTask(String name, String description, int idEpic) {
        super(name, description);
        this.idEpic = idEpic;
        this.type = TaskType.SUBTASK;
    }

    public SubTask(String name, String description, long duration, LocalDateTime startTime, int idEpic) {
        super(name, description, duration, startTime);
        this.idEpic = idEpic;
        this.type = TaskType.SUBTASK;
    }

    public SubTask(String name, String description, long duration, LocalDateTime startTime, LocalDateTime endTime, int idEpic) {
        super(name, description, duration, startTime);
        this.idEpic = idEpic;
        this.type = TaskType.SUBTASK;
        this.endTime = endTime;
    }

    public SubTask(String name, String description, Status status, long duration, LocalDateTime startTime, int idEpic) {
        super(name, description, status, duration, startTime);
        this.idEpic = idEpic;
        this.type = TaskType.SUBTASK;
    }

    public SubTask(int id, String name, String description, Status status, long duration, LocalDateTime startTime, int idEpic) {
        super(id, name, description, status, duration, startTime);
        this.idEpic = idEpic;
        this.type = TaskType.SUBTASK;
    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return idEpic == subTask.idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }


    @Override
    public String toString() {
        return super.toString() + "," + idEpic;
    }
}