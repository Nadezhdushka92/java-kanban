package taskmanager;

import taskmanager.typetask.Epic;
import taskmanager.typetask.Status;
import taskmanager.typetask.SubTask;
import taskmanager.typetask.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        //Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        System.out.println("***Создание двух задач***");
        Task task1 = new Task("Задача 1","Сдать спринт4",Status.NEW);
        Task added1Task = taskManager.addTask(task1);
        //System.out.println("Добавлена задача: " + added1Task);

        Task task2 = new Task("Задача 2","После спринта4 сдать спринт5",Status.NEW);
        Task added2Task = taskManager.addTask(task2);
        //System.out.println("Добавлена задача: " + added2Task);

        System.out.println("\n***Создание эпика с двумя позадачами***");
        Epic epic1 = new Epic("Эпик 1","Пройти обучение Java", Status.NEW);
        Epic added1Epic = taskManager.addEpic(epic1);
        //System.out.println("Добавлен эпик: " + added1Epic);

        SubTask subTask1 = new SubTask("Подзадача 1","Пройти теорию Java", Status.DONE, 1);
        SubTask added1subTask = taskManager.addSubTask(subTask1);
        //System.out.println("Добавлена подазадача: " + added1subTask);

        SubTask subTask2 = new SubTask("Подзадача 2","Пройти практику Java", Status.IN_PROGRESS, 1);
        SubTask added2subTask = taskManager.addSubTask(subTask2);
        //System.out.println("Добавлена подазадача: " + added2subTask);
        taskManager.updateStatusEpic(epic1);
        //System.out.println("Статус Epic: " + epic1.getStatus());
        //System.out.println("Статус задачи1: " + task1.getStatus());
        //System.out.println("Статус задачи2: " + task2.getStatus());

        System.out.println("\n***Создание второго эпика с одной позадачей***");
        Epic epic2 = new Epic("Эпик 2","Трудойстройтсво на Java разработчика", Status.NEW);
        Epic added2Epic = taskManager.addEpic(epic2);
        //System.out.println("Добавлен эпик: " + added2Epic);

        SubTask subTask3 = new SubTask("Подзадача 3","Успешное окончание курса Java", Status.NEW, 2);
        SubTask added3subTask = taskManager.addSubTask(subTask3);
        //System.out.println("Добавлена подзадача: " + added3subTask);

        taskManager.updateStatusEpic(epic2);
        //System.out.println("Статус Epic2: " + epic2.getStatus());

        //Распечатайте списки эпиков, задач и подзадач через System.out.println(..)
        System.out.println("\nПечать всех задач, подзадач, эпиков: ");
        System.out.println(taskManager.getListTasks());
        System.out.println(taskManager.getListSubTasks());
        System.out.println(taskManager.getListEpics());

        //Измените статусы созданных объектов, распечатайте их.
        //Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        System.out.println("\nЗадачи, подзадачи, эпики 1 до изменения статусов");
        System.out.println(task1);
        System.out.println(epic1);
        for (SubTask subTask : taskManager.getListSubTaskByIdEpic(1)) {
            System.out.println(subTask);
        }

        System.out.println("\n***Задачи, подзадачи, эпики 1 изменение статусов***");
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.DONE);
        taskManager.updateStatusEpic(epic1);

        System.out.println("\nЗадачи, подзадачи, эпики 1 после изменения статусов");
        System.out.println(task1);
        System.out.println(epic1);
        for (SubTask subTask : taskManager.getListSubTaskByIdEpic(1)) {
            System.out.println(subTask);
        }

        System.out.println("\n***Задачи, подзадачи, эпики 1 очередное изменение статусов***");
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.updateStatusEpic(epic1);

        System.out.println("\nЗадачи, подзадачи, эпики 1 после очередного изменения статусов");
        System.out.println(task1);
        System.out.println(epic1);
        for (SubTask subTask : taskManager.getListSubTaskByIdEpic(1)) {
            System.out.println(subTask);
        }

        System.out.println("\nЗадачи, подзадачи, эпики 2 до изменения статусов");
        System.out.println(task2);
        System.out.println(epic2);
        for (SubTask subTask : taskManager.getListSubTaskByIdEpic(2)) {
            System.out.println(subTask);
        }

        System.out.println("\n***Задачи, подзадачи, эпики 2 изменение статусов***");
        subTask3.setStatus(Status.DONE);
        taskManager.updateStatusEpic(epic2);

        System.out.println("\nЗадачи, подзадачи, эпики 2 после изменения статусов");
        System.out.println(task2);
        System.out.println(epic2);
        for (SubTask subTask : taskManager.getListSubTaskByIdEpic(2)) {
            System.out.println(subTask);
        }

        //И, наконец, попробуйте удалить одну из задач и один из эпиков.
        //Воспользуйтесь дебаггером среды разработки, чтобы понять логику работы программы и отладить её
        System.out.println("\n***Удаление задачи 2***");
        taskManager.deleteTask(task2.getId());

        System.out.println("\nПечать всех задач, подзадач, эпиков, после удаления задачи 2:");
        System.out.println(taskManager.getListTasks());
        System.out.println(taskManager.getListSubTasks());
        System.out.println(taskManager.getListEpics());

        System.out.println("\n***Удаление эпика 2***");
        taskManager.deleteEpic(epic2.getId());

        System.out.println("\nПечать всех задач, подзадач, эпиков, после удаления эпика 2:");
        System.out.println(taskManager.getListTasks());
        System.out.println(taskManager.getListSubTasks());
        System.out.println(taskManager.getListEpics());

        //Task updatedTask = new Task("Сделать ТЗ");
        //updatedTask.id = addedTask.id;
        }
}


