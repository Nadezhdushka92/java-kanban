package taskmanager.App;

import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;
import taskmanager.tasks.Epic;
import taskmanager.tasks.Status;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefaultTaskManager();

        //Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        System.out.println("***Создание двух задач***");
        Task task1 = new Task("Задача 1","Сдать спринт6",Status.NEW);
        Task added1Task = inMemoryTaskManager.addTask(task1);
        System.out.println("Добавлена задача: " + added1Task);

        Task task2 = new Task("Задача 2","После спринта6 сдать спринт7",Status.NEW);
        Task added2Task = inMemoryTaskManager.addTask(task2);
        System.out.println("Добавлена задача: " + added2Task);

        System.out.println("\n***Создание эпика с тремя позадачами***");
        Epic epic1 = new Epic("Эпик 1","Пройти обучение Java", Status.NEW);
        Epic added1Epic = inMemoryTaskManager.addEpic(epic1);
        System.out.println("Добавлен эпик: " + added1Epic);

        SubTask subTask1 = new SubTask("Подзадача 1","Пройти теорию Java", Status.DONE, 3);
        SubTask added1subTask = inMemoryTaskManager.addSubTask(subTask1);
        System.out.println("Добавлена подазадача: " + added1subTask);

        SubTask subTask2 = new SubTask("Подзадача 2","Пройти практику Java", Status.IN_PROGRESS, 3);
        SubTask added2subTask = inMemoryTaskManager.addSubTask(subTask2);
        System.out.println("Добавлена подазадача: " + added2subTask);

        SubTask subTask3 = new SubTask("Подзадача 3","Успешное окончание курса Java", Status.NEW, 3);
        SubTask added3subTask = inMemoryTaskManager.addSubTask(subTask3);
        System.out.println("Добавлена подзадача: " + added3subTask);

        System.out.println("Статус Epic: " + epic1.getStatus());
        System.out.println("Статус задачи1: " + added1subTask.getStatus());
        System.out.println("Статус задачи2: " + added2subTask.getStatus());
        System.out.println("Статус задачи3: " + added3subTask.getStatus());


        System.out.println("\n***Создание второго эпика без подзадач***");
        Epic epic2 = new Epic("Эпик 2","Трудойстройтсво на Java разработчика", Status.NEW);
        Epic added2Epic = inMemoryTaskManager.addEpic(epic2);
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
        }
}


