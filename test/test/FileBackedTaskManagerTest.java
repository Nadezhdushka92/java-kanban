package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.manager.FileBackedTaskManager;
import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;
import taskmanager.tasks.Epic;
import taskmanager.tasks.Status;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;
import taskmanager.tasks.TaskType;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    private TaskManager fileBackedTaskManager;

    @BeforeEach
    void initTests() {
        //String fileSavedHistory = "C:\\Users\\12345\\IdeaProjects\\java-kanban[Sprint7]\\java-kanban\\src\\resources\\saveTaskManager.csv";
        //String fileSavedHistory = Paths.get("src/resources/saveTaskManager.csv").getFileName().toString();
        File fileSavedHistory =new File("./java-kanban/src/resources/saveTaskManager.csv");
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(fileSavedHistory);
    }

    public TaskManager getFileBackedTaskManager () {
        return fileBackedTaskManager;
    }

    public void setFileBackedTaskManager (TaskManager fileBackedTaskManager) {
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Test
    public void equalsClassTaskIdAndFile() {
        //Prepare
        Task expectedTask = new Task("Задача 1", "После спринта7 сдать спринт8");
        fileBackedTaskManager.addTask(expectedTask);
        //Do
        int idTask = fileBackedTaskManager.getTaskById(expectedTask.getId()).getId();
        System.out.println(idTask);
        //Check
        Assertions.assertEquals(expectedTask.getId(),idTask);
    }

    @Test
    public void equalsClassEpicIdAndFile() {
        //Prepare
        Epic expectedEpic = new Epic("Эпик 1", "Пройти обучение Java");
        fileBackedTaskManager.addEpic(expectedEpic);
        //Do
        int idEpic = fileBackedTaskManager.getEpicsById(expectedEpic.getId()).getId();
        System.out.println(idEpic);
        //Check
        assertEquals(expectedEpic.getId(), idEpic);
    }

    @Test
    public void equalsClassSubTaskIdAndFile() {
        //Prepare
        Epic expectedEpic = new Epic("Эпик 1", "Пройти обучение Java");
        SubTask expectedSubTask = new SubTask("Подзадача 1", "Пройти практику Java", 1);
        fileBackedTaskManager.addEpic(expectedEpic);
        fileBackedTaskManager.addSubTask(expectedSubTask);
        //Do
        int idSubTask = fileBackedTaskManager.getSubTaskById(expectedSubTask.getId()).getId();
        System.out.println(idSubTask);
        //Check
        assertEquals(expectedSubTask.getId(), idSubTask);
    }
}
