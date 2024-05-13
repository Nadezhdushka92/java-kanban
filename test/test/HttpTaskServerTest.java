package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;
import taskmanager.server.HttpTaskServer;
import taskmanager.tasks.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskServerTest {
    private HttpTaskServer taskServer;
    private TaskManager taskManager;
    private Task taskTest;
    private Epic epicTest;
    private SubTask subTaskTest;
    private final URI URI_CONST = URI.create("http://localhost:8080");
    private final Gson gson = Managers.getGson();

    @BeforeEach
    public void setUp() throws IOException {
        taskTest = new Task("Модуль 2",
                "Завершить модуль 2",
                300, LocalDateTime.of(2024, 5, 12, 8, 00));
        epicTest = new Epic("Окончить обучение Java",
                "Сдать следующий модуль обучения",
                10, LocalDateTime.of(2024, 5, 12, 8, 39));
        subTaskTest = new SubTask("Изучить и сдать 3 модуль",
                "Изучить новые темы",
                600, LocalDateTime.of(2024, 5, 12, 17, 1),1);
        epicTest.delAllSubTasks();
        taskManager = Managers.getDefaultTaskManager();
        taskServer = new HttpTaskServer(taskManager);

        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    //1
    @Test
    void getTasksFromServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.addTask(taskTest);
        URI uri = URI.create(URI_CONST + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(taskTest, actual.get(0), "Задачи не совпадают");
    }

    //2
    @Test
    void getTaskByIdFromServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.addTask(taskTest);
        URI uri = URI.create(URI_CONST + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(taskTest, actual, "Задачи не совпадают");
    }

    //3
    @Test
    void postTaskCreateOnServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Task taskPost = new Task("Модуль 2",
                "Завершить модуль 2",
                300, LocalDateTime.of(2024, 5, 12, 8, 0));

        URI uri = URI.create(URI_CONST + "/tasks");
        String jsonTask = gson.toJson(taskPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonTask, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //4
    @Test
    void postUpdateTaskOnServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        taskManager.addTask(taskTest);

        Task taskPost = new Task(taskTest.getId(),"Модуль 2",
                "Завершить модуль 2",Status.NEW,
                300, LocalDateTime.now());

        taskManager.updateTask(taskPost);

        URI uri = URI.create(URI_CONST + "/tasks/1");
        String jsonTask = gson.toJson(taskPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonTask, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //5
    @Test
    void deleteListTaskFromServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URI_CONST + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //6
    @Test
    void deleteTaskByIdFromTheServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URI_CONST + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //7
    @Test
    void getListEpicsFromServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.addEpic(epicTest);
        URI uri = URI.create(URI_CONST + "/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> actual = gson.fromJson(response.body(), epicType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(epicTest, actual.get(0), "Задачи не совпадают");
    }

    //8
    @Test
    void getEpicByIdFromServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.addEpic(epicTest);
        URI uri = URI.create(URI_CONST + "/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Epic>() {
        }.getType();
        Epic actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(epicTest, actual, "Задачи не совпадают");
    }

    //9
    @Test
    void postEpicCreateOnTheServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Epic epicPost = new Epic("Окончить обучение Java",
                "Сдать следующий модуль обучения",
                0, null);

        URI uri = URI.create(URI_CONST + "/epics");
        String jsonEpic = gson.toJson(epicPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonEpic, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //10
    @Test
    void postUpdateEpicOnServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Epic epicPost = new Epic("Окончить обучение Java",
                "Сдать следующий модуль обучения",
                0, null);

        URI uri = URI.create(URI_CONST + "/epics/1");
        String jsonEpic = gson.toJson(epicPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonEpic, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //11
    @Test
    void deleteListEpicsFromTheServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URI_CONST + "/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //12
    @Test
    void deleteEpicByIdFromTheServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URI_CONST + "/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

//    //13
//    @Test
//    void getListSubTasksFromServerTest() throws IOException, InterruptedException {
//
//        HttpClient client = HttpClient.newHttpClient();
//        taskManager.addEpic(epicTest);
//        taskManager.addSubTask(subTaskTest);
//        URI uri = URI.create(URI_CONST + "/subtasks");
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(uri)
//                .GET()
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        Assertions.assertEquals(200, response.statusCode());
//
//        Type subTaskType = new TypeToken<ArrayList<SubTask>>() {
//        }.getType();
//        List<SubTask> subTask = gson.fromJson(response.body(), subTaskType);
//        List<SubTask> actual = subTask.stream()
//                .map(task -> new SubTask(task.getName(),
//                        task.getDescription(), task.getDuration(), task.getStartTime(), epicTest.getId()))
//                .collect(Collectors.toList());
//
//        Assertions.assertNotNull(actual, "Список пуст");
//        Assertions.assertEquals(1, actual.size(), "Неверное количество");
//        Assertions.assertEquals(subTaskTest, actual.getFirst(), "Задачи не совпадают");
//    }

//    //14
//    @Test
//    void getSubTaskByIdFromServerTest() throws IOException, InterruptedException {
//
//        HttpClient client = HttpClient.newHttpClient();
//        taskManager.addEpic(epicTest);
//        taskManager.addSubTask(subTaskTest);
//        URI uri = URI.create(URI_CONST + "/subtasks/2");
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(uri)
//                .GET()
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        Assertions.assertEquals(200, response.statusCode());
//
//        Type subTaskType = new TypeToken<SubTask>() {
//        }.getType();
//        SubTask subTask = gson.fromJson(response.body(), subTaskType);
//        SubTask actual = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getDuration(), subTask.getStartTime(), epicTest.getId());
//
//        Assertions.assertNotNull(actual, "Список пуст");
//        Assertions.assertEquals(subTaskTest, actual, "Задачи не совпадают");
//    }

    //15
    @Test
    void postSubTaskCreateOnServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        SubTask subTaskPost = new SubTask("Изучить и сдать 3 модуль",
                "Изучить новые темы",
                600, LocalDateTime.of(2024, 5, 12, 17, 1), epicTest.getId());

        URI uri = URI.create(URI_CONST + "/subtasks");
        String jsonEpic = gson.toJson(subTaskPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonEpic, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //16
    @Test
    void postUpdateSubTaskOnServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.addEpic(epicTest);
        taskManager.addSubTask(subTaskTest);
        SubTask subTaskPost = new SubTask(2,"Изучить и сдать 3 модуль",
                "Изучить новые темы", Status.NEW,
                600, LocalDateTime.now(),epicTest.getId());
        taskManager.updateSubTask(subTaskPost);

        URI uri = URI.create(URI_CONST + "/subtasks/1");
        String jsonEpic = gson.toJson(subTaskPost);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonEpic, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //17
    @Test
    void deleteListSubTasksFromServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URI_CONST + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //18
    @Test
    void deleteSubTasksByIdFromServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URI_CONST + "/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    //19
    @Test
    void getPrioritizedTasksFromServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.addTask(taskTest);
        URI uri = URI.create(URI_CONST + "/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(taskTest, actual.getFirst(), "Задачи не совпадают");
    }

    //20
    @Test
    void getHistoryFromServerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        taskManager.addTask(taskTest);
        taskManager.getTaskById(taskTest.getId());
        URI uri = URI.create(URI_CONST + "/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        Assertions.assertNotNull(actual, "Список пуст");
        Assertions.assertEquals(1, actual.size(), "Неверное количество");
        Assertions.assertEquals(taskTest, actual.getFirst(), "Задачи не совпадают");
    }
}