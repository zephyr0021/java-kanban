package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private int historyListSize = 0;

    Map<Integer, Node> history = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.containsKey(task.getId())) {
                remove(task.getId());
                history.put(task.getId(), linkLast(task));
            } else {
                history.put(task.getId(), linkLast(task));
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
        history.remove(id);
    }

    private Node linkLast(Task task) {
        if (task != null) {
            if (tail == null) {
                head = new Node(null, task, null);
                tail = head;
            } else {
                tail.next = new Node(tail, task, null);
                tail = tail.next;
            }
            historyListSize++;

        }
        return tail;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        int tasksSize = historyListSize;
        Node current = head;
        while (tasksSize > 0) {
            if (current.data != null) {
                tasks.add(current.data);
            }
            current = current.next;
            tasksSize--;
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node != null) {
            if (node == tail) {
                tail = tail.prev;
            } else if (node == head) {
                head = head.next;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.data = null;
                node.prev = null;
                node.next = null;
            }
            historyListSize--;
        }
    }

    private static class Node {
        public Node prev;
        public Task data;
        public Node next;

        public Node(Node prev, Task data, Node next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }

}
