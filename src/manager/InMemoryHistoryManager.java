package manager;

import entity.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

private static class Node<T> { // создаю узел 2 связного списка
    private T value;
    private Node<T> prev; // данные
    private Node<T> next; // ссылка

    public Node(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}

private Node<Task> head;
private Node<Task> tail;
private int size;
private Map<Integer,Node<Task>> taskMap; // Хэш-таблица для быстрого доступа к узлам по идентификатору задачи

    public InMemoryHistoryManager() { // это сама мапа
        taskMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {

        if (task == null) {
            return;
        }

        int taskId = task.getId();
        Node<Task> existingNode = taskMap.get(taskId);

        if (existingNode != null) {
            removeNode(existingNode);
            taskMap.remove(taskId,existingNode);
        }

        Node<Task> newNode = new Node<>(task);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            linkLast(newNode);
        }

        taskMap.put(taskId,newNode);
    }

    private void linkLast(Node<Task> newNode) {
        newNode.prev = tail;       // Предыдущий узел для нового узла - текущий хвост
        tail.next = newNode;       // Следующий узел для текущего хвоста - новый узел
        tail = newNode;            // Новый узел становится хвостом списка
        size++;                    // Увеличиваем размер списка
    }

    private void removeNode(Node<Task> node) {
        Node<Task> prev = node.prev;
        Node<Task> next = node.next;

        if (prev != null) {
            prev.next = tail;
        } else {
            head = next;
        }

        if (next != null) {
            next.prev = prev;
        } else {
            tail = prev;
        }

        node.prev = null;   // Отсоединяем node от списка
        node.next = null;
        size--;
    }

    public void remove(int id) {
        Node<Task> nodeToRemove =  taskMap.remove(id);
        if (nodeToRemove == null) {
            return;
        }
        removeNode(nodeToRemove);
        }


    public List<Task> getHistory() {
        List<Task> taskList = new ArrayList<>(size);
        Node<Task> current = head;

        while (current != null) {
            taskList.add(current.getValue());
            current = current.next;
        }
        return taskList;
    }
}


