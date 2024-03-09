package taskmanager.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;
import taskmanager.tasks.Epic;
import taskmanager.tasks.Status;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private TaskManager inMemoryTaskManager;

    @BeforeEach
    void initTests() {
        inMemoryTaskManager = Managers.getDefaultTaskManager();
    }

    @Test
        //1
    void createTask_shouldGenerateIDandSaveTask() {
        //Prepare
        Task expected = new Task(1, "Задача 1", "Сдать спринт4", Status.NEW);
        Task task1 = new Task("Задача 1", "Сдать спринт4", Status.NEW);
        //DO
        Task added1Task = inMemoryTaskManager.addTask(task1);
        //Check
        Task actual = inMemoryTaskManager.getTaskById(1);
        Assertions.assertEquals(expected, actual);
    }

    //1
    @Test
    void getTaskById_shouldReturnTask() {
        //Prepare
        Task expected = new Task(1, "Задача 2", "После спринта4 сдать спринт5", Status.NEW);
        Task task2 = new Task("Задача 2", "После спринта4 сдать спринт5", Status.NEW);
        Task added2Task = inMemoryTaskManager.addTask(task2);
        //Do
        Task actual = inMemoryTaskManager.getTaskById(1);
        //Check
        Assertions.assertEquals(expected, actual);
    }

    //2
    @Test
    void createEpic_shouldGenerateIDandSaveEpic() {
        //Prepare
        Epic expected = new Epic(1, "Эпик 1", "Пройти обучение Java", Status.NEW);
        Epic epic1 = new Epic("Эпик 1", "Пройти обучение Java", Status.NEW);
        //DO
        Epic added1Epic = inMemoryTaskManager.addEpic(epic1);
        //Check
        Epic actual = inMemoryTaskManager.getEpicsById(1);
        Assertions.assertEquals(expected, actual);
    }

    //2
    @Test
    void createSubTask_shouldGenerateIDandSaveSubTask() {
        //Prepare
        Epic epic2 = new Epic("Эпик 2", "Трудойстройтсво на Java разработчика", Status.NEW);
        Epic added2Epic = inMemoryTaskManager.addEpic(epic2);
        SubTask expected = new SubTask(2, "Подзадача 1", "Пройти теорию Java", Status.NEW, 1);
        SubTask subTask1 = new SubTask("Подзадача 1", "Пройти теорию Java", Status.NEW, 1);
        //DO
        SubTask added1subTask = inMemoryTaskManager.addSubTask(subTask1);
        //Check
        SubTask actual = inMemoryTaskManager.getSubTaskById(2);
        Assertions.assertEquals(expected, actual);
    }

    //3
    @Test
    void getTaskById_shouldSaveTaskToHistory() {
        //Prepare
        Task expected = new Task(1, "Задача 3", "После спринта5 сдать спринт6", Status.NEW);
        Task task3 = new Task("Задача 3", "После спринта5 сдать спринт6", Status.NEW);
        Task added3Task = inMemoryTaskManager.addTask(task3);
        //Do
        Task actual = inMemoryTaskManager.getTaskById(1);
        List<Task> history = inMemoryTaskManager.getHistory();
        //Check
        Assertions.assertEquals(1, history.size());
        Assertions.assertEquals(expected, history.get(0));
    }

    //3
    @Test
    void getEpicById_shouldSaveEpicToHistory() {
        //Prepare
        Epic expected = new Epic(1, "Эпик 3", "Исп.срок работы Java разработчиком", Status.NEW);
        Epic epic3 = new Epic("Эпик 3", "Исп.срок работы Java разработчиком", Status.NEW);
        Epic added3Epic = inMemoryTaskManager.addEpic(epic3);
        //Do
        Epic actual = inMemoryTaskManager.getEpicsById(1);
        List<Task> history = inMemoryTaskManager.getHistory();
        //Check
        Assertions.assertEquals(1, history.size());
        Assertions.assertEquals(expected, history.get(0));
    }

    //3
    @Test
    void getSubTaskById_shouldSaveSubTaskToHistory() {
        //Prepare
        Epic epic4 = new Epic("Эпик 4", "Работа Java разработчиком", Status.NEW);
        Epic added4Epic = inMemoryTaskManager.addEpic(epic4);
        SubTask expected = new SubTask(2, "Подзадача 2", "Пройти практику Java", Status.NEW, 1);
        SubTask subTask2 = new SubTask("Подзадача 2", "Пройти практику Java", Status.NEW, 1);
        SubTask added2SubTask = inMemoryTaskManager.addSubTask(subTask2);
        //Do
        SubTask actual = inMemoryTaskManager.getSubTaskById(2);
        List<Task> history = inMemoryTaskManager.getHistory();
        //Check
        Assertions.assertEquals(1, history.size());
        Assertions.assertEquals(expected, history.get(0));
    }

    //4
    @Test
    void addTask_shouldRewriteIdTaskIfAddNewTask() {
        //Prepare
        Task expected = new Task(1, "Задача 4", "Окончание курса Java", Status.NEW);
        Task task4 = new Task(2, "Задача 4", "Окончание курса Java", Status.NEW);
        Task added4Task = inMemoryTaskManager.addTask(task4);
        //Do
        Task actual = inMemoryTaskManager.getTaskById(1);
        //Check
        Assertions.assertEquals(expected, actual);
    }

    //5
    @Test
    void updateTask_notShouldSaveTheSameTaskToHistory() {
        //Prepare
        Task expected = new Task(1, "Задача 5", "После спринта6 сдать спринт7", Status.NEW);
        Task expected2 = new Task(1, "Задача 5", "После спринта6 сдать спринт7 с испр. зам. от ревьюера", Status.NEW);
        Task task5 = new Task("Задача 5", "После спринта6 сдать спринт7", Status.NEW);
        Task added5Task = inMemoryTaskManager.addTask(task5);

        //Do
        Task actual = inMemoryTaskManager.getTaskById(1);
        Task upd5Task = new Task(1, "Задача 5", "После спринта6 сдать спринт7 с испр. зам. от ревьюера", Status.NEW);
        inMemoryTaskManager.updateTask(upd5Task);
        Task actual2 = inMemoryTaskManager.getTaskById(1);

        List<Task> history = inMemoryTaskManager.getHistory();
        //Check
        Assertions.assertEquals(2, history.size());
        Assertions.assertEquals(expected, history.get(0));
        Assertions.assertEquals(expected2, history.get(1));
    }
}