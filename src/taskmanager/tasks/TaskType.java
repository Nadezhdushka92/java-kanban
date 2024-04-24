package taskmanager.tasks;

public enum TaskType {
    TASK("TASK"),
    EPIC("EPIC"),
    SUBTASK("SUBTASK");

    public String getValue() {
        return value;
    }

    public final String value;

    TaskType(String value) {
        this.value = value;
    }

    public TaskType getByValue() {
        for (TaskType tt: TaskType.values()) {
            if (tt.getValue().equals(value)) {
               return tt;
            }
        }
        throw new IllegalArgumentException("No Task Type with value " + value);
    }
}
