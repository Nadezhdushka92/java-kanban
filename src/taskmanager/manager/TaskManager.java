package taskmanager.manager;

import taskmanager.tasks.Status;
import taskmanager.tasks.Epic;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int idCnt = 0;

    //В мапах хранятся задачи всех типов.
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    //Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    //----------------Tasks-------------//
    //a. Получение списка всех задач.
    public ArrayList<Task> getListTasks(){
        return new ArrayList<>(tasks.values());
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
        newTask.setId(generationID());
        //System.out.println(newTask.getId());
        tasks.put(newTask.getId(), newTask);

        return newTask;
    }

    //e. Обновление задачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task updTask) {
        if (tasks.get(updTask.getId()) != null) {
            Task curTask = tasks.get(updTask.getId());
            tasks.put(updTask.getId(), updTask);
            System.out.println("Задача " + curTask + " отредактирована на - " + updTask);
        } else {
            System.out.println("Неизвестная задача");
        }
    }

    //f. Удаление задачи по идентификатору.*/
    public Task deleteTask(int id) {
        return tasks.remove(id);
    }
    //----------------EndTasks-------------//

    //----------------subTasks-------------//
    //a. Получение списка всех подзадач.
    public ArrayList<SubTask> getListSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    //b. Удаление всех подзадач.
    public void delListSubTask(int id) {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.delAllSubTasks(); //удаление привязок ID подзадач в классе Epic
            updateStatusEpic(epic);
        }
    }

    //c. Получение подзадачи по идентификатору.
    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    //d. Создание подзадачи. Сам объект должен передаваться в качестве параметра.
    public SubTask addSubTask(SubTask newSubTask) {
        if (epics.containsKey(newSubTask.getIdEpic())) {
            newSubTask.setId(generationID());
            //System.out.println("numSubTask= " + newSubTask.getId());

            int idEpic = newSubTask.getIdEpic();
            Epic epic = epics.get(idEpic);

            ArrayList<Integer> idEpicSubTasks = epic.getIdSubTask();
            idEpicSubTasks.add(newSubTask.getId());  //добавление нового ID подзадачи в Epic
            //epic.addNewSubTask(id);
            epic.setIdSubTask(idEpicSubTasks);

            subTasks.put(newSubTask.getId(), newSubTask);

            updateStatusEpic(epic);
        }
        return newSubTask;
    }

    //e. Обновление подзадачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateSubTask(SubTask updSubTask) {
        if (subTasks.containsKey(updSubTask.getId())) {
            SubTask curSubTask = subTasks.get(updSubTask.getId());
            subTasks.put(updSubTask.getId(), updSubTask);
            System.out.println("Подадача " + curSubTask + " отредактирована на - " + updSubTask);

            int idUpdEpic = updSubTask.getIdEpic();
            Epic epic = epics.get(idUpdEpic);

            subTasks.put(idUpdEpic, updSubTask);
            updateStatusEpic(epic);
        } else {
            System.out.println("Неизвестная подзадача");
        }
    }

    //f. Удаление подзадач Epica по идентификатору.
    public SubTask deleteSubTask(int id) {
        SubTask deletedSubTask = subTasks.get(id);

        Epic epic = epics.get(deletedSubTask.getIdEpic());
        //epic.getIdSubTask().remove(id);  //удаление привязки ID подзадач в классе Epic
        epic.delSubTask(id); //удаление привязки ID подзадач в классе Epic
        subTasks.remove(id);
        updateStatusEpic(epic);
        return deletedSubTask;
    }
    //----------------EndsubTasks-------------//

    //----------------Epics-------------//
    //a. Получение списка всех эпиков.
    public ArrayList<Epic> getListEpics(){
        return new ArrayList<>(epics.values());
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
        newEpic.setId(generationID());
        //System.out.println("numEPic= " + newEpic.getId());
        epics.put(newEpic.getId(), newEpic);

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
        for (int idSubTask : deletedEpic.getIdSubTask()) {
            subTasks.remove(idSubTask);
        }
        epics.remove(id);
        return deletedEpic;
    }
    //----------------EndEpics-------------//

    //Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    //хранить в эпике List<Integer>, а в методах менеджера пробегаться по этому списку и для каждого id делать subTasks.get(id).
    public ArrayList<Integer> getListSubTaskByIdEpic (int idEpic) {
        Epic epic = epics.get(idEpic);

        return epic.getIdSubTask();
    }

    /*Управление статусами осуществляется по следующему правилу:
    a. Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче.
    По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
    b. Для эпиков:
    если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
    если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
    во всех остальных случаях статус должен быть IN_PROGRESS*/
    public void updateStatusEpic(Epic epic) {
    int counterSubTaskNew = 0;
    int counterSubTaskDone = 0;
    for (int id : epic.getIdSubTask()) { //SubTask subTask : subTasks.values()
        if (subTasks.get(id).getStatus().equals(Status.NEW)) {
            counterSubTaskNew++;
            //if (counterSubTaskNew == subTasks.size()) {
                epic.setStatus(Status.NEW);
            //}
        } else if (subTasks.get(id).getStatus().equals(Status.DONE)) {
            //counterSubTaskDone++;
            //if (counterSubTaskDone == subTasks.size()) {
                epic.setStatus(Status.DONE);
            //}
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
    }

    public int generationID() {
        idCnt++;
        return idCnt;
    }

}
