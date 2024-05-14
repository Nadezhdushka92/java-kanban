package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.manager.TaskManager;
import taskmanager.tasks.Epic;
import taskmanager.tasks.Status;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {

    protected T manager;
    @BeforeEach
    public void init() {
        createTaskManager();
    }

    protected abstract T createTaskManager();

    //1
    @Test
    public void createTask_shouldGenerateIdAndSaveTaskTest() throws IOException {
        //Prepare
        //Task expected = new Task(1, "Задача 1", "Сдать спринт4", Status.NEW, 0, LocalDateTime.now());
        Task task1 = new Task("Задача 1", "Сдать спринт4", Status.NEW, 0, null);
        //DO
        Task added1Task = manager.addTask(task1);
        List<Task> taskList = manager.getListTasks();

        //Check
        assertNotNull(taskList, "Задача не найдена");
        assertEquals(taskList.getFirst(), task1, "Задачи не совпадают");
        assertEquals(1, taskList.size(), "Неверное количество задач");
        assertEquals(added1Task, taskList.getFirst(), "Задача не совпадают");
    }

    @Test
    public void TimeOfExecuteTaskTest() throws IOException {
        Task added7Task = manager.addTask(new Task("Задача 7","После спринта8 сдать спринт9", 0,
            null));
        LocalDateTime timeStartIsEmpty = added7Task.getStartTime();
        LocalDateTime timeEndIsEmpty = added7Task.getEndTime();
        long durationIsEmpty = added7Task.getDuration();

        assertNull(timeStartIsEmpty, "Время старта не Null");
        assertNull(timeEndIsEmpty, "Время конца не Null");
        assertEquals(0, durationIsEmpty, "Продолжительность не Null");

        added7Task.createTime(20, LocalDateTime.of(2024, 1, 6, 10, 00));

        LocalDateTime timeStartNotEmpty = LocalDateTime.of(2024, 1, 6, 10, 00);
        LocalDateTime timeEndNotEmpty = LocalDateTime.of(2024, 1, 6, 10, 20);
        long durationNotEmpty = 20;

        LocalDateTime timeStartTask = added7Task.getStartTime();
        LocalDateTime timeEndTask = added7Task.getEndTime();
        long durationTask = added7Task.getDuration();

        assertEquals(timeStartTask, timeStartNotEmpty, "Время старта не совпадает");
        assertEquals(timeEndTask, timeEndNotEmpty, "Время конца не совпадает");
        assertEquals(durationTask, durationNotEmpty, "Продолжительность не совпадает");
    }

    @Test
    public void createEpic_shouldGenerateIdAndSaveEpicTest() throws IOException {
        //Prepare
        //Epic expected = new Epic(1, "Эпик 1", "Пройти обучение Java", Status.NEW, 0, LocalDateTime.now());
        Epic epic1 = new Epic("Эпик 1", "Пройти обучение Java", Status.NEW, 0, LocalDateTime.now());
        //DO
        Epic added1Epic = manager.addEpic(epic1);
        List<Epic> epicList = manager.getListEpics();
        //Check
        assertNotNull(epicList, "Эпик null");
        assertEquals(1, epicList.size(), "Неверное количество эпиков");
        assertEquals(epic1, epicList.getFirst(), "Задачи не совпадают");
        assertNotNull(epic1, "Эпик не найден");
    }

    //2
    @Test
    public void createSubTask_shouldGenerateIdAndSaveSubTaskTest() throws IOException {
        //Prepare
        Epic epic2 = new Epic("Эпик 2", "Трудойстройтсво на Java разработчика", Status.NEW, 0, LocalDateTime.now());
        Epic added2Epic = manager.addEpic(epic2);
        assertNotNull(epic2, "Эпик null");
        //SubTask expected = new SubTask(2, "Подзадача 1", "Пройти теорию Java", Status.NEW, 1);
        SubTask subTask1 = new SubTask("Подзадача 1", "Пройти теорию Java", Status.NEW, 0, LocalDateTime.now(), 1);
        //DO
        SubTask added1subTask = manager.addSubTask(subTask1);
        List<SubTask> subtaskList = manager.getListSubTaskByIdEpic(epic2.getId());
        //Check
        assertNotNull(subTask1, "Подзадача не найдена");
        assertNotNull(subtaskList, "Подзадача Null");
        assertEquals(1, subtaskList.size(), "Неверное количество Подзадач");
        assertEquals(subTask1, subtaskList.getFirst(), "Подзадачи не совпадают");
    }

@Test
public void TimeOfExecuteSubTaskTest() throws IOException {
    //Prepare
    Epic added1Epic = manager.addEpic(new Epic("Эпик 1","Пройти обучение Java", 0,
            null));
    SubTask added1subTask = manager.addSubTask(new SubTask("Подзадача 1","Пройти теорию Java", 0,
            null,1));
    //Do
    final LocalDateTime timeStartIsNull = added1subTask.getStartTime();
    final LocalDateTime timeEndIsNull = added1subTask.getEndTime();
    final long durationIsEmpty = added1subTask.getDuration();

    assertNull(timeStartIsNull, "Время старта не Null");
    assertNull(timeEndIsNull, "Время конца не Null");
    assertEquals(0, durationIsEmpty, "Продолжительность не Null");

    added1subTask.createTime(30, LocalDateTime.of(2024, 5, 6, 8, 00));
    System.out.println(added1subTask);

    final LocalDateTime timeStartNotEmpty = LocalDateTime.of(2024, 5, 6, 8, 00);
    final LocalDateTime timeEndNotEmpty = LocalDateTime.of(2024, 5, 6, 8, 30);
    final long durationNotEmpty = 30;

    final LocalDateTime timeStartTask = added1subTask.getStartTime();
    final LocalDateTime timeEndTask = added1subTask.getEndTime();
    final long durationTask = added1subTask.getDuration();

    assertEquals(timeStartTask, timeStartNotEmpty, "Время старта не совпадает");
    assertEquals(timeEndTask, timeEndNotEmpty, "Время конца не совпадает");
    assertEquals(durationTask, durationNotEmpty, "Продолжительность не совпадает");
}

@Test
public void getAllTasksTest() throws IOException {
    //Prepare
    Task added3Task = manager.addTask(new Task("Задача 1","Сдать спринт8", 1,
            LocalDateTime.now().plusHours(1)));
    Task added4Task = manager.addTask(new Task("Задача 2","После спринта9 сдать спринт7", 1,
            LocalDateTime.now().plusHours(2)));
    //Do
    List<Task> tasks = manager.getListTasks();
    //Check
    assertNotNull(tasks, "Список задач пуст");
    assertEquals(2, tasks.size(), "Неверное количество задач");
}

@Test
public void getHistoryTest() throws IOException {
    //Prepare
    Task added5Task = manager.addTask(new Task("Задача 1","Сдать спринт9", 1,
            LocalDateTime.now().plusHours(1)));
    //Do
    manager.getTaskById(added5Task.getId());
    List<Task> history = manager.getHistory();
    //Check
    assertEquals(1, history.size(), "Неверный размер истории");
    assertEquals(added5Task, history.getFirst(), "Задачи не совпадают");
}

@Test
public void getPrioritizedTasksTest() throws IOException {
    Task added6Task = manager.addTask(new Task("Задача 1","Сдать Модуль 2", 1,
LocalDateTime.now().plusHours(1)));

    List<Task> prioritizedTasks = manager.getPrioritizedTasks();

    assertEquals(1, prioritizedTasks.size(), "Неверный размер отсортированных задач");
    assertEquals(added6Task, prioritizedTasks.getFirst(), "Задачи не совпадают");
}
}
