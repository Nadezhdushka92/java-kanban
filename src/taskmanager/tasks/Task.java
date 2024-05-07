package taskmanager.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected TaskType type;
    protected int id = 0;
    protected String name;
    protected String description;
    protected Status status;
    protected long duration = 0;
    protected LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.type = TaskType.TASK;
    }


    public Task(String name, String description, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        setStatus(Status.NEW);
        setType(TaskType.TASK);
    }

    public Task(String name, String description, Status status, long duration, LocalDateTime startTime) {
        //this.id = getId();
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        setType(TaskType.TASK);
    }
    public Task(int id, String name, String description, Status status, long duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        type = getType();
    }
    public Task() {

    }
    @Override
    public boolean equals ( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && type == task.type && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode () {
        return Objects.hash(type, id, name, description, status, duration, startTime);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration);
        }
        return null;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskType getType() {
        return type;
    }

    public void createTime(long duration, LocalDateTime startTime) {
        this.duration = duration;
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        String toString = Integer.toString(getId()) +
                ',' + getType() +
                "," + name +
                "," + status +
                "," + description;
        if (duration == 0) {
            return toString + "," + duration + "," + null;
        } else {
            return toString + "," + duration + "," + startTime;
        }
    }

}