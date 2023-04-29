package manager;

import entity.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

  private final List<Task> historyTask = new LinkedList<>();

    @Override
    public void add(Task task) {
        historyTask.add(task);
        if (historyTask.size() > 10) {
            historyTask.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTask;
    }
}
