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
        System.out.println("\nВывод истории getHistory() после удаления 3 задачи:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }

        inMemoryTaskManager.deleteEpic(7);//в истории не должно быть 2 эпика

        System.out.println("************************************");
        System.out.println("\nВывод истории getHistory() после удаления 2 эпика:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }

        //Измените статусы созданных объектов, распечатайте их.
        //Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
//        System.out.println("\nЗадачи, подзадачи, эпики 1 до изменения статусов");
////        System.out.println(task1);
////        System.out.println(epic1);
//        for (SubTask subs : inMemoryTaskManager.getListSubTaskByIdEpic(3)) {
//            System.out.println(subs);
//        }
//
//        System.out.println("\n***Задачи, подзадачи, эпики 1 изменение статусов***");
////        subTask1.setStatus(Status.IN_PROGRESS);
////        subTask2.setStatus(Status.DONE);
//
//        System.out.println("\nЗадачи, подзадачи, эпики 1 после изменения статусов");
////        System.out.println(task1);
//        //taskManager.updateStatusEpic(epic1);
////        System.out.println(epic1);
//        for (SubTask subs : inMemoryTaskManager.getListSubTaskByIdEpic(3)) {
//            System.out.println(subs);
//        }
//
//        System.out.println("\n***Задачи, подзадачи, эпики 1 очередное изменение статусов***");
////        subTask1.setStatus(Status.DONE);
////        subTask2.setStatus(Status.DONE);
//
//        System.out.println("\nЗадачи, подзадачи, эпики 1 после очередного изменения статусов");
////        System.out.println(task1);
//        //taskManager.updateStatusEpic(epic1);
////        System.out.println(epic1);
//        for (SubTask subs : inMemoryTaskManager.getListSubTaskByIdEpic(3)) {
//            System.out.println(subs);
//        }
//
//        System.out.println("\nЗадачи, подзадачи, эпики 2 до изменения статусов");
////        System.out.println(task2);
////        System.out.println(epic2);
//        for (SubTask subs : inMemoryTaskManager.getListSubTaskByIdEpic(6)) {
//            System.out.println(subs);
//        }
//
//        System.out.println("\n***Задачи, подзадачи, эпики 2 изменение статусов***");
////        subTask3.setStatus(Status.DONE);
//
//        System.out.println("\nЗадачи, подзадачи, эпики 2 после изменения статусов");
////        System.out.println(task2);
//        //taskManager.updateStatusEpic(epic2);
////        System.out.println(epic2);
//        for (SubTask subs : inMemoryTaskManager.getListSubTaskByIdEpic(6)) {
//            System.out.println(subs);
//        }
//
//        //И, наконец, попробуйте удалить одну из задач и один из эпиков.
//        //Воспользуйтесь дебаггером среды разработки, чтобы понять логику работы программы и отладить её
//        System.out.println("\n***Удаление задачи 2***");
////        inMemoryTaskManager.deleteTask(task2.getId());
//
//        System.out.println("\nПечать всех задач, подзадач, эпиков, после удаления задачи 2:");
//        System.out.println(inMemoryTaskManager.getListTasks());
//        System.out.println(inMemoryTaskManager.getListSubTasks());
//        System.out.println(inMemoryTaskManager.getListEpics());
//
//        System.out.println("\n***Удаление эпика 2***");
////        inMemoryTaskManager.deleteEpic(epic2.getId());
//
//        System.out.println("\nПечать всех задач, подзадач, эпиков, после удаления эпика 2:");
//        System.out.println(inMemoryTaskManager.getListTasks());
//        System.out.println(inMemoryTaskManager.getListSubTasks());
//        System.out.println(inMemoryTaskManager.getListEpics());

//Task taskTest = inMemoryTaskManager.getTaskById(1);
//        System.out.println(taskTest);
//        System.out.println("\nВывод истории tasks:");
//        for (Task task : inMemoryTaskManager.getHistory()) {
//            System.out.println(task);
//        }
//
//        Task sTaskTest = inMemoryTaskManager.getSubTaskById(4);
//        System.out.println(sTaskTest);
//        System.out.println("\nВывод истории stasks:");
//        for (Task sTask : inMemoryTaskManager.getHistory()) {
//            System.out.println(sTask);
//        }
//
//        Task epicTest = inMemoryTaskManager.getEpicsById(3);
//        System.out.println(epicTest);
//        System.out.println("\nВывод истории epics:");
//        for (Task epic : inMemoryTaskManager.getHistory()) {
//            System.out.println(epic);
//        }
        }
}


