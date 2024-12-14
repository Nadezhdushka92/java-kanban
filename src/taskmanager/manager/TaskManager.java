package taskmanager.manager;

import taskmanager.tasks.Epic;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.util.List;

public interface TaskManager {
    //Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    //----------------Tasks-------------//
    //a. Получение списка всех задач.
    List<Task> getListTasks();

    //b. Удаление всех задач.
    void delListTask();

    //c. Получение задачи по идентификатору.
    Task getTaskById(int id);

    //d. Создание задачи. Сам объект должен передаваться в качестве параметра.
    Task addTask(Task newTask);

    //e. Обновление задачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    Task updateTask(Task updTask);

    //f. Удаление задачи по идентификатору.*/
    Task deleteTask(int id);

    //----------------subTasks-------------//
    //a. Получение списка всех подзадач.
    List<SubTask> getListSubTasks();

    //b. Удаление всех подзадач.
    void delListSubTask();

    //c. Получение подзадачи по идентификатору.
    SubTask getSubTaskById(int id);

    //d. Создание подзадачи. Сам объект должен передаваться в качестве параметра.
    SubTask addSubTask(SubTask newSubTask);

    //e. Обновление подзадачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    SubTask updateSubTask(SubTask updSubTask);

    //f. Удаление подзадач Epica по идентификатору.
    SubTask deleteSubTask(int id);

    //----------------Epics-------------//
    //a. Получение списка всех эпиков.
    List<Epic> getListEpics();

    //b. Удаление всех эпиков.
    void delListEpics();

    //c. Получение по идентификатору.
    Epic getEpicsById(int id);

    //d. Создание эпика. Сам объект должен передаваться в качестве параметра.
    Epic addEpic(Epic newEpic);

    //e. Обновление эпика. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    Epic updateEpic(Epic updEpic);

    //f. Удаление эпика по идентификатору.
    Epic deleteEpic(int id);

    //Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    //хранить в эпике List<Integer>, а в методах менеджера пробегаться по этому списку и для каждого id делать subTasks.get(id).
    List<SubTask> getListSubTaskByIdEpic(int idEpic);

    //Хранение истории
    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

}
