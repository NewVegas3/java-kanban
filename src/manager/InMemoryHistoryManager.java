package manager;

import entity.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> historyTask = new ArrayList<>();

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
