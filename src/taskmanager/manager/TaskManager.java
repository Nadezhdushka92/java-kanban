package taskmanager.manager;

import taskmanager.tasks.Epic;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    //Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    //----------------Tasks-------------//
    //a. Получение списка всех задач.
    ArrayList<Task> getListTasks();

    //b. Удаление всех задач.
    void delListTask();

    //c. Получение задачи по идентификатору.
    Task getTaskById(int id);

    //d. Создание задачи. Сам объект должен передаваться в качестве параметра.
    Task addTask(Task newTask);

    //e. Обновление задачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateTask(Task updTask);

    //f. Удаление задачи по идентификатору.*/
    Task deleteTask(int id);

    //----------------subTasks-------------//
    //a. Получение списка всех подзадач.
    ArrayList<SubTask> getListSubTasks();

    //b. Удаление всех подзадач.
    void delListSubTask();

    //c. Получение подзадачи по идентификатору.
    SubTask getSubTaskById(int id);

    //d. Создание подзадачи. Сам объект должен передаваться в качестве параметра.
    SubTask addSubTask(SubTask newSubTask);

    //e. Обновление подзадачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateSubTask(SubTask updSubTask);

    //f. Удаление подзадач Epica по идентификатору.
    SubTask deleteSubTask(int id);

    //----------------Epics-------------//
    //a. Получение списка всех эпиков.
    ArrayList<Epic> getListEpics();

    //b. Удаление всех эпиков.
    void delListEpics();

    //c. Получение по идентификатору.
    Epic getEpicsById(int id);

    //d. Создание эпика. Сам объект должен передаваться в качестве параметра.
    Epic addEpic(Epic newEpic);

    //e. Обновление эпика. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateEpic(Epic updEpic);

    //f. Удаление эпика по идентификатору.
    Epic deleteEpic(int id);

    //Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    //хранить в эпике List<Integer>, а в методах менеджера пробегаться по этому списку и для каждого id делать subTasks.get(id).
    ArrayList<SubTask> getListSubTaskByIdEpic(int idEpic);

    /*Управление статусами осуществляется по следующему правилу:
        a. Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче.
        По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
        b. Для эпиков:
        если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
        если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
        во всех остальных случаях статус должен быть IN_PROGRESS*/
    void updateStatusEpic(Epic epic);

    //Хранение истории
    List<Task> getHistory();
}
