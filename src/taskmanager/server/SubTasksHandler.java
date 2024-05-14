package taskmanager.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;
import taskmanager.tasks.Status;
import taskmanager.tasks.SubTask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;


public class SubTasksHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public SubTasksHandler(TaskManager manager) {
        this.manager = manager;
        gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                subTasksGet(exchange);
                break;
            case "POST":
                subTasksPost(exchange);
                break;
            case "DELETE":
                subTasksDelete(exchange);
                break;
        }
    }

    private void subTasksGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String response;

        if (Pattern.matches("^/subtasks$", path)) {
            List<SubTask> subTasks = manager.getListSubTasks();
            response = gson.toJson(subTasks);
            sendText(exchange, response);
        } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/subtasks/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                SubTask subTask = manager.getSubTaskById(id);
                if (subTask != null) {
                    SubTask gettedSubTask = new SubTask(subTask.getId(), subTask.getName(),
                            subTask.getDescription(), Status.valueOf(subTask.getStatus().toString()), subTask.getDuration(),
                            subTask.getStartTime(), subTask.getIdEpic());
                    response = gson.toJson(gettedSubTask);
                    sendText(exchange, response);
                }
                writeResponse(exchange, "Подзадача с id = " + pathId + " не найдена.",
                        HttpStatusCode.NOT_FOUND.getCode());

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId,
                        HttpStatusCode.NOT_ACCEPTABLE.getCode());
            }
        }
    }

    private void subTasksPost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("^/subtasks$", path)) {

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            SubTask reqToAddSubTask = gson.fromJson(body, SubTask.class);
            SubTask addedSubTask = manager.addSubTask(reqToAddSubTask);
            if (addedSubTask != null) {
                System.out.println("Подзадача создана");
                exchange.sendResponseHeaders(HttpStatusCode.CREATED.getCode(), 0);
                exchange.close();

            } else {
                writeResponse(exchange, "Подзадача пересекается с существующими",
                        HttpStatusCode.NOT_ACCEPTABLE.getCode());
            }
        } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/subtasks/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                SubTask subTaskRequest = gson.fromJson(body, SubTask.class);
                SubTask addedSubTask = manager.updateSubTask(subTaskRequest);
                if (addedSubTask != null) {
                    System.out.println("Подзадача обновлена");
                    exchange.sendResponseHeaders(HttpStatusCode.CREATED.getCode(), 0);
                    exchange.close();

                } else {
                    writeResponse(exchange, "Подзадача пересекается с существующими",
                            HttpStatusCode.NOT_ACCEPTABLE.getCode());
                }

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId,
                        HttpStatusCode.NOT_ACCEPTABLE.getCode());
            }
        }
    }

    private void subTasksDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("^/subtasks$", path)) {
            manager.delListSubTask();
            System.out.println("Все подзадачи удалены");
            exchange.sendResponseHeaders(HttpStatusCode.CREATED.getCode(), 0);
            exchange.close();
        } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
            String pathId = path.replaceFirst("/subtasks/", "");
            int id = parsePathId(pathId);
            if (id > 0) {
                manager.deleteSubTask(id);
                System.out.println("Подзадача удалена");
                exchange.sendResponseHeaders(HttpStatusCode.CREATED.getCode(), 0);
                exchange.close();

            } else {
                writeResponse(exchange, "Некорректный id =  " + pathId + pathId,
                        HttpStatusCode.NOT_ACCEPTABLE.getCode());
            }
        }
    }

    private void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), resp.length);
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