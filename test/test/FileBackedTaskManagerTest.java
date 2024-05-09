package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanager.manager.FileBackedTaskManager;
import taskmanager.tasks.Epic;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
//src/java-kanban/test/resources/saveTaskManagerTest.csv
public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>  {
    private final File fileSavedHistory = new File(".\\java-kanban\\test\\resources\\saveTaskManagerTest.csv");

    @Override
    public FileBackedTaskManager createTaskManager() {
        return manager = new FileBackedTaskManager(fileSavedHistory);
    }

//    @AfterEach
//    public void afterEach() {
//        try {
//            File file = new File("./java-kanban/test/resources/saveTaskManagerTest.csv");
//            Files.delete(file.toPath());
//        } catch (IOException exception) {
//            System.out.println(exception.getMessage());
//        }
//    }

    @Test
    public void equalsClassTaskIdAndFile() {
        //Prepare
        Task expectedTask = new Task("Задача 1", "После спринта8 сдать спринт9", 10,
                LocalDateTime.of(2024, 5, 6, 10, 00));
        manager.addTask(expectedTask);
        //Do
        int idTask = manager.getTaskById(expectedTask.getId()).getId();
        //System.out.println("idTask = " + idTask);
        //Check
        Assertions.assertEquals(expectedTask.getId(),idTask, "Задачи не равны");
    }

    @Test
    public void equalsClassEpicIdAndFile() {
        //Prepare
        Epic expectedEpic = new Epic("Эпик 1", "Пройти обучение Java", 30,
                LocalDateTime.of(2024, 5, 6, 10, 30));
        manager.addEpic(expectedEpic);
        //Do
        int idEpic = manager.getEpicsById(expectedEpic.getId()).getId();
        //System.out.println("idEpic = " + idEpic);
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
        manager.addEpic(expectedEpic);
        manager.addSubTask(expectedSubTask);
        //Do
        int idSubTask = manager.getSubTaskById(expectedSubTask.getId()).getId();
        //System.out.println("idSubTask = " + idSubTask);
        //Check
        assertEquals(expectedSubTask.getId(), idSubTask, "Подзадачи не равны");
    }
}
