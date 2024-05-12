package taskmanager.server;


import com.sun.net.httpserver.HttpServer;
import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final int port = 8080;
    private final HttpServer server;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubTasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryTaskHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedTaskHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getDefaultTaskManager());
        httpTaskServer.start();
        //httpTaskServer.stop();
    }

    public void start() {
        System.out.println("Старт сервера на порту " + port);
        server.start();
    }

    public void stop() {
        System.out.println("Остановка сервера на порту " + port);
        server.stop(0);
    }
}