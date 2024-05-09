package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanager.manager.FileBackedTaskManager;
import taskmanager.tasks.Epic;
import taskmanager.tasks.Status;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>  {
    File fileSavedHistory = new File(".\\java-kanban\\test\\resources\\saveFileTaskManagerTest.csv");//src/java-kanban/test/resources/saveTaskManagerTest.csv

    @Override
    public FileBackedTaskManager createTaskManager() {
        return manager = new FileBackedTaskManager(fileSavedHistory);
    }

    @Override
    public FileBackedTaskManager createLoadFileTaskManager() {
        return fileManager = FileBackedTaskManager.loadFromFile(fileSavedHistory);

    }

//    @AfterEach
//    public void afterEach() {
//        try {
//            File file = new File(".\\java-kanban\\test\\resources\\saveTaskManagerTest.csv");
//            Files.delete(file.toPath());
//        } catch (IOException exception) {
//            System.out.println(exception.getMessage());
//        }
//    }

    @Test
    public void equalsClassTaskIdAndFileTest() {
        //Prepare
        Task expectedTask = new Task("Задача 1", "После спринта8 сдать спринт9", 10,
                LocalDateTime.of(2024, 5, 9, 10, 00));
        manager.addTask(expectedTask);
        //Do
        int idTask = manager.getTaskById(expectedTask.getId()).getId();
        //System.out.println("idTask = " + idTask);
        //Check
        Assertions.assertEquals(expectedTask.getId(),idTask, "Задачи не равны");
    }

    @Test
    public void equalsClassEpicIdAndFileTest() {
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
    public void equalsClassSubTaskIdAndFileTest() {
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

    @Test
    public void emptyTasksEpicsSubtasksTest() {
        assertEquals(Collections.EMPTY_LIST, manager.getListTasks());
        assertEquals(Collections.EMPTY_LIST, manager.getListEpics());
        assertEquals(Collections.EMPTY_LIST, manager.getListSubTasks());
    }

    @Test
    public void emptyHistoryTest() {
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void saveTasksEpicSubtaskTest() throws IOException {
        Task task = new Task("2 модуль", "Завершить обучение", Status.NEW, 0, null);
        manager.addTask(task);

        Epic epic = new Epic("3 модуль", "Начать обучение", Status.NEW, 0, LocalDateTime.now());
        manager.addEpic(epic);

        SubTask subtask = new SubTask("Замечания и улучшения", "Улучшить TaskMangaer", Status.NEW, 9, LocalDateTime.now(), epic.getId());
        manager.addSubTask(subtask);

        assertEquals(List.of(task), manager.getListTasks());
        assertEquals(List.of(epic), manager.getListEpics());
        assertEquals(List.of(subtask), manager.getListSubTasks());

    }

    @Test
    public void loadFromFileTest() {
        Task addedTask1 = new Task("2 модуль", "Завершить обучение без замечаний", Status.NEW, 0, LocalDateTime.now());
        manager.addTask(addedTask1);

        manager.getTaskById(addedTask1.getId());
//        fileManager = manager;
//        Task uploadedTask1 = fileManager.getTaskById(addedTask1.getId());
//        Assertions.assertEquals(addedTask1.toString(), uploadedTask1.toString(), "Задачи не совпадают");
    }
}
