package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.manager.FileBackedTaskManager;
import taskmanager.tasks.Epic;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    public void initTests() {
        //String fileSavedHistory = "C:\\Users\\12345\\IdeaProjects\\java-kanban[Sprint7]\\java-kanban\\src\\resources\\saveTaskManager.csv";
        //String fileSavedHistory = Paths.get("src/resources/saveTaskManager.csv").getFileName().toString();
        File fileSavedHistory = new File("./java-kanban/test/resources/saveTaskManagerTest.csv");
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(fileSavedHistory);
    }

    @Test
    public void equalsClassTaskIdAndFile() {
        //Prepare
        Task expectedTask = new Task("Задача 1", "После спринта8 сдать спринт9", 10,
                LocalDateTime.of(2024, 5, 6, 10, 00));
        fileBackedTaskManager.addTask(expectedTask);
        //Do
        int idTask = fileBackedTaskManager.getTaskById(expectedTask.getId()).getId();
        System.out.println("idTask = " + idTask);
        //Check
        Assertions.assertEquals(expectedTask.getId(),idTask, "Задачи не равны");
    }

    @Test
    public void equalsClassEpicIdAndFile() {
        //Prepare
        Epic expectedEpic = new Epic("Эпик 1", "Пройти обучение Java", 30,
                LocalDateTime.of(2024, 5, 6, 10, 30));
        fileBackedTaskManager.addEpic(expectedEpic);
        //Do
        int idEpic = fileBackedTaskManager.getEpicsById(expectedEpic.getId()).getId();
        System.out.println("idEpic = " + idEpic);
        //Check
        assertEquals(expectedEpic.getId(), idEpic, "Эпики не равны");
    }

    @Test
    public void equalsClassSubTaskIdAndFile() {
        //Prepare
        Epic expectedEpic = new Epic("Эпик 1", "Пройти обучение Java", 60,
                LocalDateTime.of(2024, 5, 6, 11, 30));
        SubTask expectedSubTask = new SubTask("Подзадача 1", "Пройти практику Java", 61,
                LocalDateTime.of(2024, 5, 6, 11, 30),1);
        fileBackedTaskManager.addEpic(expectedEpic);
        fileBackedTaskManager.addSubTask(expectedSubTask);
        //Do
        int idSubTask = fileBackedTaskManager.getSubTaskById(expectedSubTask.getId()).getId();
        System.out.println("idSubTask = " + idSubTask);
        //Check
        assertEquals(expectedSubTask.getId(), idSubTask, "Подзадачи не равны");
    }
}
