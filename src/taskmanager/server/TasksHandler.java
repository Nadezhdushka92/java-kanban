package taskmanager.server;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.httpclient.HttpStatus;
import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;
import taskmanager.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class TasksHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
        gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                tasksGet(exchange);
                break;
            case "POST":
                tasksPost(exchange);
                break;
            case "DELETE":
                tasksDelete(exchange);
                break;
        }
    }

    private void tasksGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String response;

        if (Pattern.matches("^/tasks$", path)) {

            List<Task> tasks = manager.getListTasks();
            response = gson.toJson(tasks);
            sendText(exchange, response);

        } else if (Pattern.matches("^/tasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/tasks/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                Task task = manager.getTaskById(id);
                if (task != null) {
                    response = gson.toJson(task);
                    sendText(exchange, response);
                }
                writeResponse(exchange, "Задачи с id = " + pathId + " нет.",
                        HttpStatus.SC_NOT_FOUND);

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId,
                        HttpStatus.SC_NOT_FOUND);
            }
        }
    }

    private void tasksPost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("^/tasks$", path)) {

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task reqToAddTask = gson.fromJson(body, Task.class);
            Task addedTask = manager.addTask(reqToAddTask);
            if (addedTask != null) {
                System.out.println("Задача создана");
                exchange.sendResponseHeaders(HttpStatus.SC_CREATED, 0);
                exchange.close();

            } else {
                writeResponse(exchange, "Задача пересекается с существующими",
                        HttpStatus.SC_NOT_ACCEPTABLE);
            }
        } else if (Pattern.matches("^/tasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/tasks/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Task reqToUpdTask = gson.fromJson(body, Task.class);
                Task updTask = manager.updateTask(reqToUpdTask);
                if (updTask != null) {
                    System.out.println("Задача обновлена");
                    exchange.sendResponseHeaders(HttpStatus.SC_CREATED, 0);
                    exchange.close();

                } else {
                    writeResponse(exchange, "Задача пересекается с существующими",
                            HttpStatus.SC_NOT_ACCEPTABLE);
                }

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId,
                        HttpStatus.SC_NOT_ACCEPTABLE);
            }
        }
    }

    private void tasksDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("^/tasks$", path)) {
            manager.delListTask();
            System.out.println("Задачи удалены");
            exchange.sendResponseHeaders(HttpStatus.SC_CREATED, 0);
            exchange.close();

        } else if (Pattern.matches("^/tasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/tasks/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                manager.deleteTask(id);
                System.out.println("Задача удалена");
                exchange.sendResponseHeaders(HttpStatus.SC_CREATED, 0);
                exchange.close();

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId,
                        HttpStatus.SC_NOT_ACCEPTABLE);
            }
        }
    }

    private void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(HttpStatus.SC_OK, resp.length);
        exchange.getResponseBody().write(resp);
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(StandardCharsets.UTF_8));
        }
        exchange.close();
    }

    private int parsePathId(String pathId) {
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

}