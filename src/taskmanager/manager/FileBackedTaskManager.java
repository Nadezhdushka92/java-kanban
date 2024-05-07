package taskmanager.manager;

import taskmanager.exception.ManagerSaveException;
import taskmanager.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File fileSave;

    public FileBackedTaskManager(File fileSave) {
        this.fileSave = fileSave;
    }

    public static void main(String[] args) {
        File fileSave = new File("./java-kanban/src/resources/saveTaskManager.csv");
        //String fileSave = "C:\\Users\\12345\\IdeaProjects\\java-kanban[Sprint7]\\java-kanban\\src\\resources\\saveTaskManager.csv";
        //String fileSave = Paths.get("./java-kanban/src/resources/saveTaskManager.csv").getFileName().toString();

        FileBackedTaskManager newBackedTasksManager = loadFromFile(fileSave);

        Task added1Task = newBackedTasksManager.addTask(new Task("Задача 1","Сдать спринт6", 1,
                LocalDateTime.now().plusHours(1)));

        Task added2Task = newBackedTasksManager.addTask(new Task("Задача 2","После спринта6 сдать спринт7", 1,
                LocalDateTime.now().plusHours(2)));

        Epic added1Epic = newBackedTasksManager.addEpic(new Epic("Эпик 1","Пройти обучение Java", 1,
                LocalDateTime.now().plusHours(3)));

        SubTask added1subTask = newBackedTasksManager.addSubTask(new SubTask("Подзадача 1","Пройти теорию Java", 1,
                LocalDateTime.now().plusHours(4),3));

        SubTask added2subTask = newBackedTasksManager.addSubTask(new SubTask("Подзадача 2","Пройти практику Java", 1,
                LocalDateTime.now().plusHours(5), 3));

        SubTask added3subTask = newBackedTasksManager.addSubTask(new SubTask("Подзадача 3","Успешное окончание курса Java", 1,
                LocalDateTime.now().plusHours(6), 3));

        Epic added2Epic = newBackedTasksManager.addEpic(new Epic("Эпик 2","Трудойстройтсво на Java разработчика", 1,
                LocalDateTime.now().plusDays(1)));

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

        //newBackedTasksManager.deleteSubTask(6);//в истории не должно быть 3ей задачи
        //newBackedTasksManager.deleteEpic(7);//в истории не должно быть 2 эпика
    }

    public void save() {
        final String taskFields = "id,type,name,status,description,duration,localtime,epic";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileSave,  StandardCharsets.UTF_8));
            //Writer writer = new FileWriter(fileSave);
            //BufferedWriter bufferedWriter = new BufferedWriter(writer);

            writer.write(taskFields);
            writer.write("\n");

            if (!tasks.isEmpty()) {
                for (Task task : tasks.values()) {
                    writer.write(task.toString() + "\n");

                }
            }
            if (!epics.isEmpty()) {
                for (Epic epic : epics.values()) {
                    writer.write(epic.toString() + "\n");
                }
            }
            if (!subTasks.isEmpty()) {
                for (SubTask subTask : subTasks.values()) {
                    writer.write(subTask.toString() + "\n");
                }
            }

            writer.write("\n");
            writer.write(historyToString(historyManager));//Managers.getDefaultTaskManager())
            writer.close();
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

    public static FileBackedTaskManager loadFromFile(File file) {
    //Cчитываем содержимое файла
    FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

    String taskString = parsingFile(file.getParentFile().toString());
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

        int mID = 0;
        String[] word = s.split(",");
        boolean isTime = Long.parseLong(word[5]) > 0;
        LocalDateTime startTime = LocalDateTime.parse(word[6]);
        long duration = Long.parseLong(word[5]);
        try {
            switch (word[1]) {
                case "TASK":
                    int idT = Integer.parseInt(word[0]);
                    Task task = new Task(word[2], word[4], Status.valueOf(word[3]),Long.parseLong(word[5]), startTime);
                    //task.setId(Integer.parseInt(word[0]));
                    //addTask(task);
                    if (idT > mID) {
                        mID = idT;
                    }
                    tasks.put(mID, task);//addTask(task);
                    if (isTime) {
                        task.createTime(duration, startTime);
                    }
                    //task.setStatus(Status.valueOf(word[3]));
                    //updateTask(task);
                    return task;
            case "EPIC":
                int idE = Integer.parseInt(word[0]);
                Epic epic = new Epic(word[2], word[4], Status.valueOf(word[3]), Long.parseLong(word[5]), startTime);
                //epic.setId(Integer.parseInt(word[0]));
                //addEpic(epic);
                if (idE > mID) {
                    mID = idE;
                }
                epics.put(mID, epic);//addTask(task);
                //epic.setStatus(Status.valueOf(word[3]));
                //updateEpic(epic);
            return epic;
            case "SUBTASK":
                int idS = Integer.parseInt(word[0]);
                SubTask subTask = new SubTask(word[2], word[4], Status.valueOf(word[3]), Long.parseLong(word[5]),startTime, Integer.parseInt(word[7]));
                //subTask.setId(Integer.parseInt(word[0]));
                //addSubTask(subTask);
                if (idS > mID) {
                    mID = idS;
                }
                subTasks.put(mID, subTask);
                if (isTime) {
                    subTask.createTime(duration, startTime);
                }
                //взятие ID эпика:
                int tmpIdEpic = subTask.getIdEpic();
                Epic epicOfSubTask = epics.get(tmpIdEpic);
                //запоминаем в лист номера подазач определенного эпика
                var idSubTasks = epicOfSubTask.getIdSubTask();
                //добавляем в лист с номерами подазад номер подзадачи, связанный с нужным эпиком
                idSubTasks.add(Integer.parseInt(word[0]));
                //восстанавливаем номера подзадач для опредленного эпика
                epicOfSubTask.setIdSubTask(idSubTasks);
                //idEpicSubTasks.add(Integer.parseInt(word[0]));
                //epics.get(idEpic).setIdSubTask(idEpicSubTasks);//добавление нового ID подзадачи в Epic
                //subTask.setStatus(Status.valueOf(word[3]));
                //updateSubTask(subTask);
                return subTask;
            }
        } catch (IllegalArgumentException exception) {
            throw new ManagerSaveException("Ошибка при проверке перечислений", exception);
        }
        return null;
    }

    private static List<Integer> historyFromString(File file) {
        if (file == null) {
            return new ArrayList<>();
        }
        String[] arrayHistory = Objects.requireNonNull(parsingFile(file.getParentFile().toString())).split("\n");
        if (arrayHistory.length < 8) {
            return new ArrayList<>();
        }
        String stringOfHistory = arrayHistory[arrayHistory.length - 1];
        String[] lineHistory = stringOfHistory.split(",");
        List<Integer> historyList = new ArrayList<>();

        for (String line : lineHistory) {
            int id = Integer.parseInt(line);
            //if (id > 0) {
                historyList.add(Integer.parseInt(line));

            //}
        }
        return historyList;//return null;
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
