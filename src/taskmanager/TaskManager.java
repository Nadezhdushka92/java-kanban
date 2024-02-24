package taskmanager;

import taskmanager.typetask.Status;
import taskmanager.typetask.Epic;
import taskmanager.typetask.SubTask;
import taskmanager.typetask.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public static int idTask = 0;
    public static int idSubTask = 0;
    public static int idEpic = 0;
    //В мапах хранятся задачи всех типов.
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    //Методы для каждого из типа задач(Задача/Эпик/Подзадача):

    //----------------Tasks-------------//
    //a. Получение списка всех задач.
    public ArrayList<Task> getListTasks(){
        ArrayList<Task> listTasks = new ArrayList<>();
        for (int id : tasks.keySet()) {
            Task task = tasks.get(id);
            listTasks.add(task);
        }
        return listTasks;
    }

    //b. Удаление всех задач.
    public void delListTask() {
        tasks.clear();
    }

    //c. Получение задачи по идентификатору.
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    //d. Создание задачи. Сам объект должен передаваться в качестве параметра.
    public Task addTask(Task newTask) {
        int id = generationIDtask();
        tasks.put(id, newTask);
        newTask.setId(id);
        return newTask;
    }

    //e. Обновление задачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task updTask) {
        if (tasks.containsKey(updTask.getId())) {
            Task curTask = tasks.get(updTask.getId());
            tasks.put(updTask.getId(), updTask);
            System.out.println("Задача " + curTask + " отредактирована на - " + updTask);
        } else {
            System.out.println("Неизвестная задача");
        }
    }

    //f. Удаление задачи по идентификатору.*/
    public Task deleteTask(int id) {
        Task deletedTask = tasks.get(id);
        tasks.remove(id);
        return deletedTask;
    }
    //----------------EndTasks-------------//

    //----------------subTasks-------------//
    //a. Получение списка всех подзадач.
    public ArrayList<SubTask> getListSubTasks() {
        ArrayList<SubTask> listSubTasks = new ArrayList<>();
        for (int id : subTasks.keySet()) {
            SubTask subTask = subTasks.get(id);
            listSubTasks.add(subTask);
        }
        return listSubTasks;
    }

    //b. Удаление всех подзадач.
    public void delListSubTask(int id) {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            //
        }
    }

    //c. Получение подзадачи по идентификатору.
    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    //d. Создание подзадачи. Сам объект должен передаваться в качестве параметра.
    public SubTask addSubTask(SubTask newSubTask) {
        int id = generationIDsubTask();
        subTasks.put(id, newSubTask);

        int idEpic = newSubTask.getIdEpic();
        Epic epic = epics.get(idEpic);
        epic.addSubTask(newSubTask);
        newSubTask.setId(id);
        return newSubTask;
    }

    //e. Обновление подзадачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateSubTask(SubTask updSubTask) {
        if (tasks.containsKey(updSubTask.getId())) {
            SubTask curSubTask = subTasks.get(updSubTask.getId());
            tasks.put(updSubTask.getId(), updSubTask);
            System.out.println("Подадача " + curSubTask + " отредактирована на - " + updSubTask);

            int idUpdEpic = updSubTask.getIdEpic();
            Epic epic = epics.get(idUpdEpic);

            ArrayList<SubTask> listSubTask = epic.getSubTasks();
            listSubTask.set(idUpdEpic, updSubTask);
        } else {
            System.out.println("Неизвестная подзадача");
        }
    }

    //f. Удаление подзадачи по идентификатору.
    public SubTask deleteSubTask(int id) {
        SubTask deletedSubTask = subTasks.get(id);

        Epic epic = epics.get(deletedSubTask.getIdEpic());
        epic.getSubTasks().remove(id);
        return deletedSubTask;
    }
    //----------------EndsubTasks-------------//

    //----------------Epics-------------//
    //a. Получение списка всех эпиков.
    public ArrayList<Epic> getListEpics(){
        ArrayList<Epic> listEpics = new ArrayList<>();
        for (int id : epics.keySet()) {
            Epic epic = epics.get(id);
            listEpics.add(epic);
        }
        return listEpics;
    }

    //b. Удаление всех эпиков.
    public void delListEpics() {
        epics.clear();
        subTasks.clear();
    }

    //c. Получение по идентификатору.
    public Epic getEpicsById(int id) {
        return epics.get(id);
    }

    //d. Создание эпика. Сам объект должен передаваться в качестве параметра.
    public Epic addEpic(Epic newEpic) {
        int id = generationIDepic();
        epics.put(id, newEpic);
        newEpic.setId(id);
        return newEpic;
    }

    //e. Обновление эпика. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateEpic(Epic updEpic) {
        if (epics.containsKey(updEpic.getId())) {
            Task curEpic = tasks.get(updEpic.getId());
            tasks.put(updEpic.getId(), updEpic);
            System.out.println("Эпик " + curEpic + " отредактирован на - " + updEpic);
        } else {
            System.out.println("Неизвестный эпик");
        }
    }

    //f. Удаление эпика по идентификатору.
    public Epic deleteEpic(int id) {
        Epic deletedEpic = epics.get(id);
        for (SubTask subTask : deletedEpic.getSubTasks()) {
            subTasks.remove(subTask.getIdEpic());
        }
        epics.remove(id);
        return deletedEpic;
    }
    //----------------EndEpics-------------//

    //Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    public ArrayList<SubTask> getListSubTaskByIdEpic (int idEpic) {
        Epic epic = epics.get(idEpic);
        return epic.getSubTasks();
    }

    /*Управление статусами осуществляется по следующему правилу:
    a. Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче.
    По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
    b. Для эпиков:
    если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
    если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
    во всех остальных случаях статус должен быть IN_PROGRESS*/
    public void updateStatusEpic(Epic epic) {
    ArrayList<SubTask> listSubTasks = epic.getSubTasks();
    int counterSubTaskNew = 0;
    int counterSubTaskDone = 0;
    for (SubTask subTask : listSubTasks) {
        if (subTask.getStatus().equals(Status.NEW)) {
            counterSubTaskNew++;
            if (counterSubTaskNew == listSubTasks.size()) {
                epic.setStatus(Status.NEW);
            }
        } else if (subTask.getStatus().equals(Status.DONE)) {
            counterSubTaskDone++;
            if (counterSubTaskDone == listSubTasks.size()) {
                epic.setStatus(Status.DONE);
            }
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    Task task = tasks.get(epic.getId());
    if (epic.getStatus().equals(Status.NEW)) {
        task.setStatus(Status.NEW);
    } else if (epic.getStatus().equals(Status.DONE)) {
        task.setStatus(Status.DONE);
    } else {
        task.setStatus(Status.IN_PROGRESS);
    }
    }

    public static int generationIDtask() {
        idTask++;
        return idTask;
    }

    public static int generationIDsubTask() {
        idSubTask++;
        return idSubTask;
    }
    public static int generationIDepic() {
        idEpic++;
        return idEpic;
    }
}
