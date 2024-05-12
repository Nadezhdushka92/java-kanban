package taskmanager.manager;
import taskmanager.exception.ManagerValidateException;
import taskmanager.tasks.Epic;
import taskmanager.tasks.Status;

import taskmanager.tasks.SubTask;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import taskmanager.tasks.Task;

public class InMemoryTaskManager implements TaskManager {
    /*В мапах хранятся задачи всех типов.*/
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    protected int idCnt = 0;
    protected final Set<Task> prioritizedTasks = new TreeSet<>(new StartTimeComparator());

    public InMemoryTaskManager() {
    }

    //Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    //----------------Tasks-------------//
    //a. Получение списка всех задач.
    @Override
    public List<Task> getListTasks() {
        return new ArrayList<>(tasks.values());
    }

    //b. Удаление всех задач.
    @Override
    public void delListTask() {
        for (Task task : tasks.values()) {
            removeByIdFromHistory(task.getId());
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    //c. Получение задачи по идентификатору.
    @Override
    public Task getTaskById(int id) {
        Task t = tasks.get(id);
        if (t != null) {                     //(tasks.containsKey(id)) {
            historyManager.add(t);           //historyManager.add(tasks.get(id));
            return t;
        }
        return null;                            //return tasks.get(id);
    }

    //d. Создание задачи. Сам объект должен передаваться в качестве параметра.
    //В методе addTask и addSubTask есть проверка на перекрытие времени задач.
    //Проверка нарушений условия невозможности одновременного выполнения более чем одной задачи.
    //Проверки временных пересечений в методах добавления задач.
    @Override
    public Task addTask(Task newTask) {
        newTask.setId(generationID());
        if (!tasks.containsKey(newTask.getId())) {
            taskValidation(newTask);
            tasks.put(newTask.getId(), newTask);
            prioritizedTasks.add(newTask);
            return newTask;
        }
        System.out.println("Задача " + newTask.getName() + " не добавлена");
        return null;
    }

    //e. Обновление задачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public Task updateTask(Task updTask) {
        Task curTask = tasks.get(updTask.getId());
        if (curTask != null) {
            tasks.put(updTask.getId(), updTask);
            prioritizedTasks.add(updTask);

            System.out.println("Задача " + curTask + " отредактирована на - " + updTask);
            return updTask;
        } else {
            System.out.println("Неизвестная задача");
        }
        return null;
    }

    //f. Удаление задачи по идентификатору.*/
    @Override
    public Task deleteTask(int id) {
        Task deletedTask = tasks.remove(id);
        prioritizedTasks.remove(tasks.get(id));

        if (deletedTask == null) {
            System.out.println("Задача с указанным ID отсутствует");
            return null;
        } else {
            removeByIdFromHistory(id);
        }
        return deletedTask;
    }
    //----------------EndTasks-------------//

    //----------------subTasks-------------//
    //a. Получение списка всех подзадач.
    @Override
    public List<SubTask> getListSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    //b. Удаление всех подзадач.
    @Override
    public void delListSubTask() {
        for (Epic epic : epics.values()) {
            epic.delAllSubTasks(); //удаление привязок ID подзадач в классе Epic
            updateStatusEpic(epic);
            //removeByIdFromHistory(epic.getId()); //checked and corrected to Sprint6?
        }
        //epics.clear(); //checked and corrected to Sprint6?
        for (SubTask subTask : subTasks.values()) {
            removeByIdFromHistory(subTask.getId());
        }
        subTasks.clear();
    }

    //c. Получение подзадачи по идентификатору.
    @Override
    public SubTask getSubTaskById(int id) {
        SubTask sT = subTasks.get(id);
        if (sT != null) {
            historyManager.add(sT);
        }
        return sT;
    }

    //d. Создание подзадачи. Сам объект должен передаваться в качестве параметра.
    @Override
    public SubTask addSubTask(SubTask newSubTask) {
        Epic epic = epics.get(newSubTask.getIdEpic());
        newSubTask.setId(generationID());
        if (!subTasks.containsKey(newSubTask.getId())) {

            //cохраняем подзадачу, если существует эпик для нее
            if (epic != null) {
                List<Integer> idEpicSubTasks = epic.getIdSubTask();
                idEpicSubTasks.add(newSubTask.getId());  //добавление нового ID подзадачи в Epic
                epic.setIdSubTask(idEpicSubTasks);
                taskValidation(newSubTask);
                subTasks.put(newSubTask.getId(), newSubTask);

                updateTimesEpic(epic);
                updateStatusEpic(epic);
            }
            prioritizedTasks.add(newSubTask);
            return newSubTask;
        }
        System.out.println("Задача " + newSubTask.getName() + " не добавлена");
        return null;

    }

    //e. Обновление подзадачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public SubTask updateSubTask(SubTask updSubTask) {
        Epic epic = epics.get(updSubTask.getIdEpic());

        if (epic != null) {
            SubTask curSubTask = subTasks.get(updSubTask.getId());
            subTasks.put(updSubTask.getId(), updSubTask);
            System.out.println("Подадача " + curSubTask + " отредактирована на - " + updSubTask);

            subTasks.put(updSubTask.getIdEpic(), updSubTask);
            prioritizedTasks.add(updSubTask);

            updateTimesEpic(epic);
            updateStatusEpic(epic);
            return updSubTask;
        } else {
            System.out.println("Неизвестная подзадача");
        }
            System.out.println("Подзадача " + updSubTask.getName() + " не обновлена");
        return null;
    }

    //f. Удаление подзадач Epica по идентификатору.
    @Override
    public SubTask deleteSubTask(int id) {
        SubTask deletedSubTask = subTasks.remove(id);
        prioritizedTasks.remove(subTasks.get(id));

        if (deletedSubTask == null) {
            System.out.println("Нет такой подзадачи");
            return null;
        } else {
            Epic epic = epics.get(deletedSubTask.getIdEpic());

            List<Integer> idEpicSubTasks = epic.getIdSubTask();
            for (Integer i : idEpicSubTasks) {
                if (i.equals(id)) {
                    epic.delSubTask(idEpicSubTasks.indexOf(i)); //удаление привязки ID подзадач в классе Epic
                }
            }

            removeByIdFromHistory(id);

            updateStatusEpic(epic);
        }
        return deletedSubTask;
    }
    //----------------EndsubTasks-------------//

    //----------------Epics-------------//
    //a. Получение списка всех эпиков.
    @Override
    public List<Epic> getListEpics() {
        return new ArrayList<>(epics.values());
    }

    //b. Удаление всех эпиков.
    @Override
    public void delListEpics() {
        for (Epic epic : epics.values()) {
            removeByIdFromHistory(epic.getId());
        }
        epics.clear();
        for (SubTask subTask : subTasks.values()) {
            removeByIdFromHistory(subTask.getId());
        }
        subTasks.clear();
    }

    //c. Получение по идентификатору.
    @Override
    public Epic getEpicsById(int id) {
        Epic e = epics.get(id);
        if (e != null) {
            historyManager.add(e);         //historyManager.add(epics.get(id));
        }
        return e;                          //return epics.get(id);
    }

    //d. Создание эпика. Сам объект должен передаваться в качестве параметра.
    @Override
    public Epic addEpic(Epic newEpic) {
        newEpic.setId(generationID());
        epics.put(newEpic.getId(), newEpic);

        return newEpic;
    }

    //e. Обновление эпика. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public Epic updateEpic(Epic updEpic) {
        if (epics.containsKey(updEpic.getId())) {
            Epic curEpic = epics.get(updEpic.getId());
            epics.put(updEpic.getId(), updEpic);
            System.out.println("Эпик " + curEpic + " отредактирован на - " + updEpic);
            return updEpic;
        } else {
            System.out.println("Неизвестный эпик");
        }
        return null;
    }

    //f. Удаление эпика по идентификатору.
    @Override
    public Epic deleteEpic(int id) {
        Epic deletedEpic = epics.remove(id);
        removeByIdFromHistory(id);
        if (deletedEpic == null) {
            System.out.println("Эпик с указанным ID отсутсвует");
            return null;
        } else {
            for (int idSubTask : deletedEpic.getIdSubTask()) {
                 subTasks.remove(idSubTask);
                removeByIdFromHistory(idSubTask);
                prioritizedTasks.remove(subTasks.get(idSubTask));

            }
        }
        return deletedEpic;
    }
    //----------------EndEpics-------------//

    //Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    //хранить в эпике List<Integer>, а в методах менеджера пробегаться по этому списку и для каждого id делать subTasks.get(id).
    @Override
    public List<SubTask> getListSubTaskByIdEpic(int idEpic) {
        List<SubTask> subs = new ArrayList<>();
        for (Integer id : epics.get(idEpic).getIdSubTask()) {
            subs.add(subTasks.get(id));
        }
        return subs;
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
        for (int id : epic.getIdSubTask()) {
            if (subTasks.get(id).getStatus().equals(Status.NEW)) {
                counterSubTaskNew++;
                if (counterSubTaskNew == epic.getIdSubTask().size()) {
                    epic.setStatus(Status.NEW);
                }
            } else if (subTasks.get(id).getStatus().equals(Status.DONE)) {
                counterSubTaskDone++;
                if (counterSubTaskDone == epic.getIdSubTask().size()) {
                    epic.setStatus(Status.DONE);
                }
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
        if (epic.getIdSubTask() == null || (epic.getIdSubTask().isEmpty())) {
            epic.setStatus(Status.NEW);
        }

        if (counterSubTaskNew != 0 && (counterSubTaskDone != 0)) {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public void removeByIdFromHistory(int id) {
        historyManager.remove(id);
    }

    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory()); //
    }

    //В методе updateTimesEpic для класса Epic предусмотрена логика расчета startTime и endTime
    //которая обнуляет startTime и endTime, если список подзадач пуст.
    private void updateTimesEpic(Epic epic) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        long duration = 0;

        //взятие ID эпика
        List<SubTask> subTasksList = getListSubTaskByIdEpic(epic.getId());

        if (!subTasksList.isEmpty()) {
            for (SubTask subTask : subTasksList) {
                if (subTask.getDuration() == 0) {
                    continue;
                }

                if (startTime == null || subTask.getStartTime().isBefore(startTime)) { //проверка на начало
                    startTime = subTask.getStartTime();

                }
                if (endTime == null || subTask.getEndTime().isAfter(endTime)) {
                    endTime = subTask.getEndTime();

                }

                duration = duration + subTask.getDuration();
            }
            if (startTime != null) {
                epic.createTime(duration, startTime);
                epic.setEndTime(endTime);
            }
        } else {
            if (epic.getStartTime() != null) {
                epic.setStartTime(null);
            }
            if (epic.getEndTime() != null) {
                epic.setEndTime(null);
            }
        }

    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public Stream<Boolean> taskValidation(Task task) {
        return getPrioritizedTasks().stream()
                .filter(t -> t.getStartTime() != null)
                .filter(t -> t.getStartTime() != task.getStartTime())
                .map(t -> {
                     if (t.getStartTime().isBefore(task.getEndTime()) &&
                            t.getEndTime().isAfter(task.getStartTime())
                            || t.getStartTime().isBefore(task.getStartTime())
                             && t.getEndTime().isBefore(task.getEndTime())
                             || t.getStartTime().isAfter(t.getEndTime())
                             && t.getEndTime().isAfter(task.getEndTime())) {
                         return true;
                     } else {
                         throw new ManagerValidateException("Ошибка валидации");
                     }
                });
    }

    public int generationID() {
        idCnt++;
        return idCnt;
    }

}
