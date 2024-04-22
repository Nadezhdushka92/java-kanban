package taskmanager.manager;

import taskmanager.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(task, node.task) && Objects.equals(nextTask, node.nextTask) && Objects.equals(prevTask, node.prevTask);
        }

        @Override
        public int hashCode() {
            return Objects.hash(task, nextTask, prevTask);
        }
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            removeNode(historyMap.remove(task.getId()));
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.remove(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {

        if (historyMap.isEmpty()) {
            Node node = new Node(task,null, null);
            tail = node;
            head = node;
            historyMap.put(task.getId(),node);
        } else {
            Node nextNode = new Node(task, tail, null);
            tail.nextTask = nextNode;
            //historyMap.put(tail.task.getId(),tail);
            tail = nextNode;
            historyMap.put(task.getId(), nextNode);
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
        return tasks;
    }

    private void removeNode(Node node) {
        if (node != null) {
            if (node.nextTask == null && node.prevTask == null) {
                head = null;
                tail = null;
            } else if (node.nextTask != null && node.prevTask != null) {
                node.nextTask.prevTask = node.prevTask;
                node.prevTask.nextTask = node.nextTask;
            } else if (node.nextTask != null) {
                node.nextTask.prevTask = null;
                head = node.nextTask;
            } else if (node.prevTask != null) {
                node.prevTask.nextTask = null;
                tail = node.prevTask;
            }
            historyMap.remove(node.task.getId());
        }
    }
}
