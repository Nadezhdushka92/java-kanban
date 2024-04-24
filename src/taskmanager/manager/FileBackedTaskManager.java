package taskmanager.manager;

import taskmanager.exception.ManagerSaveException;
import taskmanager.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    protected String fileSave;

    public FileBackedTaskManager(String fileSave) {
        this.fileSave = fileSave;
    }

    public static void main(String[] args) {
        String fileSave = "C:\\Users\\12345\\IdeaProjects\\java-kanban[Sprint7]\\java-kanban\\src\\resources\\saveTaskManager.csv";

        FileBackedTaskManager newBackedTasksManager = loadFromFile(fileSave);

        Task added1Task = newBackedTasksManager.addTask(new Task("Задача 1","Сдать спринт6"));

        Task added2Task = newBackedTasksManager.addTask(new Task("Задача 2","После спринта6 сдать спринт7"));

        Epic added1Epic = newBackedTasksManager.addEpic(new Epic("Эпик 1","Пройти обучение Java"));

        SubTask added1subTask = newBackedTasksManager.addSubTask(new SubTask("Подзадача 1","Пройти теорию Java", 3));

        SubTask added2subTask = newBackedTasksManager.addSubTask(new SubTask("Подзадача 2","Пройти практику Java", 3));

        SubTask added3subTask = newBackedTasksManager.addSubTask(new SubTask("Подзадача 3","Успешное окончание курса Java", 3));

        Epic added2Epic = newBackedTasksManager.addEpic(new Epic("Эпик 2","Трудойстройтсво на Java разработчика"));

        System.out.println("\nПечать всех задач, подзадач, эпиков: ");
        System.out.println(newBackedTasksManager.getListTasks());
        System.out.println(newBackedTasksManager.getListSubTasks());
        System.out.println(newBackedTasksManager.getListEpics());

        newBackedTasksManager.getSubTaskById(added3subTask.getId());
        newBackedTasksManager.getSubTaskById(added2subTask.getId());
        newBackedTasksManager.getTaskById(added1Task.getId());
        newBackedTasksManager.getEpicsById(added2Epic.getId());
        newBackedTasksManager.getTaskById(added2Task.getId());
        newBackedTasksManager.getEpicsById(added1Epic.getId());
        newBackedTasksManager.getSubTaskById(added1subTask.getId());

        newBackedTasksManager.deleteSubTask(6);//в истории не должно быть 3ей задачи
        newBackedTasksManager.deleteEpic(7);//в истории не должно быть 2 эпика
    }

    private void save() {
        try {
            Writer writer = new FileWriter(fileSave);

            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            String taskFields = "id,type,name,status,description,epic";
            bufferedWriter.write(taskFields);
            bufferedWriter.write("\n");

            if (!tasks.isEmpty()) {
                for (Task task : tasks.values()) {
                    bufferedWriter.write(task.toString() + "\n");
                }
            }
            if (!epics.isEmpty()) {
                for (Epic epic : epics.values()) {
                    bufferedWriter.write(epic.toString() + "\n");
                }
            }
            if (!subTasks.isEmpty()) {
                for (SubTask subTask : subTasks.values()) {
                    bufferedWriter.write(subTask.toString() + "\n");
                }
            }

            bufferedWriter.write("\n");
            bufferedWriter.write(historyToString(historyManager));//Managers.getDefaultTaskManager())
            bufferedWriter.close();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при записи.", exception);
        }
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> tasksFromHistory = manager.getHistory();//Managers.getDefaultTaskManager()
        StringBuilder taskString = new StringBuilder();
        taskString.append("\n");

        for (Task task : tasksFromHistory) {
            taskString.append(task.getId());
            taskString.append(",");
        }

        taskString.deleteCharAt(taskString.length() - 1);
        return taskString.toString();
    }

    public static FileBackedTaskManager loadFromFile(String file) {
    //Cчитываем содержимое файла
    FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

    String taskString = parsingFile(file);
    if (taskString == null) {
        return fileBackedTaskManager;
    }
    String[] lineTask = taskString.split("\n");
    List<Integer> idTasksFromFile = historyFromString(file);

    for (int i = 1; i < lineTask.length; i++) {
        if (lineTask[i].isEmpty()) {
            break;
        }
        Task task = fileBackedTaskManager.fromString(lineTask[i]);
        if (task != null) {
            for (Integer id : idTasksFromFile) {
                if (task.getId() == id) {
                    fileBackedTaskManager.historyManager.add(task);
                }
            }
        }
    }
    fileBackedTaskManager.save();
    return fileBackedTaskManager;
}

    private Task fromString(String s) {
        List<Integer> idEpicSubTasks = new ArrayList<>();

        String[] word = s.split(",");
        try {
        switch (word[1]) {
            case "TASK":
                Task task = new Task(word[2], word[4], Status.valueOf(word[3]));
                //task.setId(Integer.parseInt(word[0]));
                //addTask(task);
                tasks.put(Integer.parseInt(word[0]), task);//addTask(task);
                //task.setStatus(Status.valueOf(word[3]));
                //updateTask(task);
                return task;

            case "EPIC":
                Epic epic = new Epic(word[2], word[4], Status.valueOf(word[3]));
                //epic.setId(Integer.parseInt(word[0]));
                //addEpic(epic);
                epics.put(Integer.parseInt(word[0]), epic);//addTask(task);
                //epic.setStatus(Status.valueOf(word[3]));
                //updateEpic(epic);
                return epic;

            case "SUBTASK":
                SubTask subTask = new SubTask(word[2], word[4], Status.valueOf(word[3]), Integer.parseInt(word[5]));
                //subTask.setId(Integer.parseInt(word[0]));
                //addSubTask(subTask);
                subTasks.put(Integer.parseInt(word[0]), subTask);

                //взятие ID эпика:
                int idEpic = subTask.getIdEpic();
                idEpicSubTasks.add(Integer.parseInt(word[0]));
                epics.get(idEpic).setIdSubTask(idEpicSubTasks);//добавление нового ID подзадачи в Epic

                //subTask.setStatus(Status.valueOf(word[3]));
                //updateSubTask(subTask);
                return subTask;
        }
        } catch (IllegalArgumentException exception) {
            throw new ManagerSaveException("Ошибка при проверке перечислений", exception);
        }
        return null;
    }

    private static List<Integer> historyFromString(String file) {
        if (file == null) {
            return new ArrayList<>();
        }
        String[] arrayHistory = parsingFile(file).split("\n");
        if (arrayHistory.length < 8) {
            return new ArrayList<>();
        }
        String stringOfHistory = arrayHistory[arrayHistory.length - 1];
        String[] lineHistory = stringOfHistory.split(",");
        List<Integer> historyList = new ArrayList<>();

        for (String line : lineHistory) {
            historyList.add(Integer.parseInt(line));
        }
        return historyList;
    }

    private static String parsingFile(String path) {
        try {
            return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @Override
    public List<Task> getListTasks() {
        List<Task> list = super.getListTasks();
        save();
        return list;
    }

    @Override
    public void delListTask() {
        save();
        super.delListTask();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Task addTask(Task newTask) {
        Task task = super.addTask(newTask);
        save();
        return task;
    }

    @Override
    public void updateTask(Task updTask) {
        super.updateTask(updTask);
        save();
    }

    @Override
    public Task deleteTask(int id) {
        Task task = super.deleteTask(id);
        save();
        return task;
    }

    @Override
    public List<SubTask> getListSubTasks() {
        List<SubTask> list = super.getListSubTasks();
        save();
        return list;
    }

    @Override
    public void delListSubTask() {
        save();
        super.delListSubTask();
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public SubTask addSubTask(SubTask newSubTask) {
        SubTask subTask = super.addSubTask(newSubTask);
        save();
        return subTask;
    }

    @Override
    public void updateSubTask(SubTask updSubTask) {
        super.updateSubTask(updSubTask);
        save();
    }

    @Override
    public SubTask deleteSubTask(int id) {
        SubTask subTask = super.deleteSubTask(id);
        save();
        return subTask;
    }

    @Override
    public List<Epic> getListEpics() {
        List<Epic> list = super.getListEpics();
        save();
        return list;
    }

    @Override
    public void delListEpics() {
        super.delListEpics();
        save();
    }

    @Override
    public Epic getEpicsById(int id) {
        Epic epic = super.getEpicsById(id);
        save();
        return epic;
    }

    @Override
    public Epic addEpic(Epic newEpic) {
        Epic epic = super.addEpic(newEpic);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic updEpic) {
        super.updateEpic(updEpic);
        save();
    }

    @Override
    public Epic deleteEpic(int id) {
        Epic epic = super.deleteEpic(id);
        save();
        return epic;
    }

    @Override
    public List<SubTask> getListSubTaskByIdEpic(int idEpic) {
        List<SubTask> list = super.getListSubTaskByIdEpic(idEpic);
        save();
        return list;
    }


}
