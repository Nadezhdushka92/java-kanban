package taskmanager.manager;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.time.LocalDateTime;

public class Managers { //static?
    private Managers() {

    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(); //getDefaultHistoryManager()
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultFileBackedTaskManager() {
        return new FileBackedTaskManager(new File("./java-kanban/test/resources/saveTaskManagerFile.csv"));
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
