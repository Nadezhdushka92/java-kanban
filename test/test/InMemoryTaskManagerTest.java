package test;

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
    void createTask_shouldGenerateIdAndSaveTask() {
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
        Task expected = new Task(1,"Задача 2", "После спринта4 сдать спринт5",Status.NEW);
        Task task2 = new Task(2,"Задача 2", "После спринта4 сдать спринт5",Status.NEW);
        Task added2Task = inMemoryTaskManager.addTask(task2);
        //Do
        Task actual = inMemoryTaskManager.getTaskById(1);
        //Check
        Assertions.assertEquals(expected, actual);
    }

    //2
    @Test
    void createEpic_shouldGenerateIdAndSaveEpic() {
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
    void createSubTask_shouldGenerateIdAndSaveSubTask() {
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
        Assertions.assertEquals(expected, history.getFirst());
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
        Assertions.assertEquals(expected, history.getFirst());
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
        Assertions.assertEquals(expected, history.getFirst());
    }

    //4
    @Test
    void addTask_shouldRewriteIdTaskIfAddNewTask() {
        //Prepare
        Task expected = new Task(1, "Задача 4", "Окончание курса Java", Status.NEW);
        Task task4 = new Task("Задача 4", "Окончание курса Java", Status.NEW);
        Task added4Task = inMemoryTaskManager.addTask(task4);
        //Do
        Task actual = inMemoryTaskManager.getTaskById(1);
        //Check
        Assertions.assertEquals(expected, actual);
    }

    //5
    @Test
    void addTwoTaskAndOneUpdateTask_ShouldSaveTwoTasksToHistory() {
        //Prepare
        Task firstExpected = new Task(1, "Задача 5", "После спринта6 сдать спринт7", Status.NEW);
        //Task task5 = new Task("Задача 5", "После спринта6 сдать спринт7 с испр. зам. от ревьюера", Status.NEW);

        //Sprint6
        Task secondExpected = new Task(2,"Задача 6", "После спринта7 сдать спринт8", Status.NEW);
        //Task task6 = new Task("Задача 6", "После спринта7 сдать спринт8 с испр. зам. от ревьюера", Status.NEW);

        Task added5Task = inMemoryTaskManager.addTask(firstExpected);
        //Sprint6
        Task added6Task = inMemoryTaskManager.addTask(secondExpected);
        //Do;
        Task upd1Task = new Task(1, "Задача 5", "После спринта6 сдать спринт7 с испр. зам. от ревьюера!", Status.NEW);
        inMemoryTaskManager.updateTask(upd1Task);
        Task actual1 = inMemoryTaskManager.getTaskById(1);
        Task actual2 = inMemoryTaskManager.getTaskById(2);

        List<Task> history = inMemoryTaskManager.getHistory();
        //Check
        Assertions.assertEquals(2, history.size());
        Assertions.assertEquals(actual1, history.getFirst());
        Assertions.assertEquals(actual2, history.getLast());
    }

    //6
    @Test
    void addTwoTaskAndOneUpdateTaskAndDelTaskOne_ShouldSaveOneTasksToHistory() {
        //Prepare
        Task firstExpected = new Task(1, "Задача 5", "После спринта6 сдать спринт7", Status.NEW);

        //Sprint6
        Task secondExpected = new Task(2,"Задача 6", "После спринта7 сдать спринт8", Status.NEW);

        Task added6Task = inMemoryTaskManager.addTask(firstExpected);
        //Sprint6
        Task added7Task = inMemoryTaskManager.addTask(secondExpected);
        //Do
        Task actual1 = inMemoryTaskManager.getTaskById(1);

        Task upd1Task = new Task(1, "Задача 5", "После спринта6 сдать спринт7 с испр. зам. от ревьюера!", Status.NEW);
        inMemoryTaskManager.updateTask(upd1Task);
        Task actual2 = inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.deleteTask(2);

        List<Task> history = inMemoryTaskManager.getHistory();
        //Check
        Assertions.assertEquals(1, history.size());
        Assertions.assertEquals(firstExpected, history.getFirst());
    }

    //7
    @Test
    void getEpicById_shouldSaveSubTaskToHistory() {
        //Prepare
        Epic expectedEpic7 = new Epic(1,"Эпик 7", "Работа Java разработчиком", Status.NEW);
        Epic added7Epic = inMemoryTaskManager.addEpic(expectedEpic7);
        SubTask expectedSubTask = new SubTask(2, "Подзадача 2", "Пройти практику Java", Status.NEW, 1);
        SubTask added2SubTask = inMemoryTaskManager.addSubTask(expectedSubTask);
        //Do
        Epic actualEpic = inMemoryTaskManager.getEpicsById(1);
        SubTask actual = inMemoryTaskManager.getSubTaskById(2);
        List<Task> history = inMemoryTaskManager.getHistory();
        //Check
        //Assertions.assertEquals(2, history.size());
        Assertions.assertEquals(expectedEpic7, history.getFirst());
        Assertions.assertEquals(expectedSubTask, history.getLast());
    }
}