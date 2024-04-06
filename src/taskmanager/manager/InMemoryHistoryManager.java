package taskmanager.manager;

import taskmanager.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> historyMap;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        historyMap = new HashMap<>();
    }

    public static class Node {
        protected Task task;
        protected Node nextTask;
        protected Node prevTask;

        public Node(Task task, Node prevTask, Node nextTask) {
            this.task = task;
            this.prevTask = prevTask;
            this.nextTask = nextTask;
        }
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            //tasksHistoryList.add(task);
            //Храним историю в LinkedList, чтобы удаление первого элемента было быстрее
            //if (tasksHistoryList.size() > 10) {
            //    tasksHistoryList.removeFirst();
            if (historyMap.containsKey(task.getId())) {
                removeNode(historyMap.remove(task.getId()));
            }
            //}
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            //tasksHistoryList.remove(id);
            removeNode(historyMap.remove(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        //return tasksHistoryList;
        return getTasks();
    }

    private void linkLast(Task task) {
        if (historyMap.isEmpty()) {
            Node node = new Node(task,null, null);
            tail = node;
            head = node;
            historyMap.put(task.getId(),node);
        } else {
            Node lastNode = new Node(task, tail, null);
            tail.nextTask = lastNode; //node = historyMap.get(task.getId()); head.prevTask = node.nextTask;
            historyMap.put(tail.task.getId(),tail);
            tail = lastNode;
            historyMap.put(task.getId(), lastNode);
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node savedHistory = head;
        if (head != null) {
            tasks.add(head.task);
            while (savedHistory.nextTask != null) {
               savedHistory = savedHistory.nextTask;
               tasks.add(savedHistory.task);
            }
        }
        return new ArrayList<>(tasks);
    }

    private void removeNode(Node node) {
        if (node.equals(head) && node.equals(tail)) {
            head = null;
            tail = null;
        } else if (node.equals(head)) {
            node.nextTask.prevTask = null;
            head = node.nextTask;
        } else if (node.equals(tail)) {
            node.prevTask.nextTask = null;
            tail = node.prevTask;
        } else {
            node.prevTask.nextTask = node.nextTask;
            node.nextTask.prevTask = node.prevTask;
        }
        historyMap.remove(node.task.getId());
    }
}
