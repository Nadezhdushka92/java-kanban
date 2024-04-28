package taskmanager.App;

import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;
import taskmanager.tasks.Epic;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        TaskManager inMemoryTaskManager = Managers.getDefaultTaskManager();
        //String fileSavedHistory = "C:\\Users\\12345\\IdeaProjects\\java-kanban[Sprint7]\\java-kanban\\src\\resources\\saveTaskManager.csv";

        //FileBackedTaskManager backedTasksManager = new FileBackedTaskManager(fileSavedHistory);

        //List<String> strings = Files.readAllLines(Paths.get("resources/saveTaskManager.csv"));//StandardCharsets.UTF_8
        //Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        System.out.println("***Создание двух задач***");

        Task added1Task = inMemoryTaskManager.addTask(new Task("Задача 1","Сдать спринт6"));
        System.out.println("Добавлена задача: " + added1Task);

        Task added2Task = inMemoryTaskManager.addTask(new Task("Задача 2","После спринта6 сдать спринт7"));
        System.out.println("Добавлена задача: " + added2Task);

        System.out.println("\n***Создание эпика с тремя позадачами***");
        Epic added1Epic = inMemoryTaskManager.addEpic(new Epic("Эпик 1","Пройти обучение Java"));
        System.out.println("Добавлен эпик: " + added1Epic);

        SubTask added1subTask = inMemoryTaskManager.addSubTask(new SubTask("Подзадача 1","Пройти теорию Java", 3));
        System.out.println("Добавлена подазадача: " + added1subTask);

        SubTask added2subTask = inMemoryTaskManager.addSubTask(new SubTask("Подзадача 2","Пройти практику Java", 3));
        System.out.println("Добавлена подазадача: " + added2subTask);

        SubTask added3subTask = inMemoryTaskManager.addSubTask(new SubTask("Подзадача 3","Успешное окончание курса Java", 3));
        System.out.println("Добавлена подзадача: " + added3subTask);

        System.out.println("\n***Создание второго эпика без подзадач***");
        Epic added2Epic = inMemoryTaskManager.addEpic(new Epic("Эпик 2","Трудойстройтсво на Java разработчика"));
        System.out.println("Добавлен эпик: " + added2Epic);

        //Распечатайте списки эпиков, задач и подзадач через System.out.println(..)
        System.out.println("\nПечать всех задач, подзадач, эпиков: ");
        System.out.println(inMemoryTaskManager.getListTasks());
        System.out.println(inMemoryTaskManager.getListSubTasks());
        System.out.println(inMemoryTaskManager.getListEpics());

        inMemoryTaskManager.getSubTaskById(added3subTask.getId());
        inMemoryTaskManager.getSubTaskById(added2subTask.getId());
        inMemoryTaskManager.getTaskById(added1Task.getId());
        inMemoryTaskManager.getEpicsById(added2Epic.getId());
        inMemoryTaskManager.getTaskById(added2Task.getId());
        inMemoryTaskManager.getEpicsById(added1Epic.getId());
        inMemoryTaskManager.getSubTaskById(added1subTask.getId());

        System.out.println("************************************");
        System.out.println("\nВывод истории getHistory() до удаления 3 подзадачи:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }

        inMemoryTaskManager.deleteSubTask(6);//в истории не должно быть 3ей задачи

        System.out.println("************************************");
        System.out.println("\nВывод истории getHistory() после удаления 3 подзадачи:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }

        inMemoryTaskManager.deleteEpic(7);//в истории не должно быть 2 эпика

        System.out.println("************************************");
        System.out.println("\nВывод истории getHistory() после удаления 2 эпика:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        //FileBackedTaskManager newBackedTasksManager = (FileBackedTaskManager) FileBackedTaskManager.loadFromFile(fileSavedHistory);

    }
}


