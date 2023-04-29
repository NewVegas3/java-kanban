package manager;

import entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;
    private Map<Integer, SimpleTask> simpleTasks = new HashMap<>();
    private Map<Integer, Epic> epicTasks = new HashMap<>();
    private Map<Integer, Subtask> subTasks = new HashMap<>();

    @Override
    public List<SimpleTask> showSimpleTask() {
        return new ArrayList<>(simpleTasks.values());
    }

    @Override
    public List<Epic> getEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public List<Subtask> showSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public SimpleTask getSimple(int simpleId) {
        historyManager.add(simpleTasks.get(simpleId));
        return simpleTasks.get(simpleId);
    }

    @Override
    public Epic getEpic(int epicId) {
        historyManager.add(epicTasks.get(epicId));
        return epicTasks.get(epicId);
    }

    @Override
    public Subtask getSubtask(int subTaskId) {
        historyManager.add(subTasks.get(subTaskId));
        return subTasks.get(subTaskId);
    }

    @Override
    public List<Subtask> getSubtasksEpic(int epicId) {
        Epic epic = epicTasks.get(epicId);
        ArrayList<Subtask> subtaskOfEpic = new ArrayList<>();
        for (Integer id : epic.getSubtasksOfEpic()) {
            subtaskOfEpic.add(subTasks.get(id));
        }
        return subtaskOfEpic;
    }


    @Override
    public void deleteAllSimpleTasks() {
        simpleTasks.clear();
    }

    @Override
    public void deletionAllEpic() {
        epicTasks.clear();
        subTasks.clear();
    }

    @Override
    public void deletionAllSubtask() {
        subTasks.clear();
        for (Epic epic : epicTasks.values()) {
            epic.getSubtasksOfEpic().clear();
            updateStatus(epic);
        }
    }

    @Override
    public void removeSimpleTask(int simpleTaskId) {
        simpleTasks.remove(simpleTaskId);
    }

    @Override
    public void removeEpicTask(int epicId) {
        Epic epic = epicTasks.get(epicId);
        for (Integer id : epic.getSubtasksOfEpic()) {
            subTasks.remove(id);
        }
        epicTasks.remove(epicId);
    }

    @Override
    public void removeSubtask(int id) {
        int epicId = subTasks.get(id).getEpicId();
        Epic epic = epicTasks.get(epicId);
        epicTasks.get(epicId).getSubtasksOfEpic().remove(Integer.valueOf(id)); // очень надеюсь что правильно...
        updateStatus(epic);
        subTasks.remove(id);
    }

    @Override
    public void addSimpleTask(SimpleTask task) {
        task.setId(nextId);
        simpleTasks.put(task.getId(), task);
        nextId++;
    }

    @Override
    public void updateSimpleTask(SimpleTask task) {
        simpleTasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId);
        epicTasks.put(epic.getId(), epic);
        nextId++;
    }

    public void updateEpic(Epic epic) {
        epicTasks.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (epicTasks.containsKey(subtask.getEpicId())) {
            subtask.setId(nextId);
            subTasks.put(subtask.getId(), subtask);
            nextId++;
            Epic epic = epicTasks.get(subtask.getEpicId());
            epic.getSubtasksOfEpic().add(subtask.getId());
            updateStatus(epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subTasks.put(subtask.getId(), subtask);
        Epic epic = epicTasks.get(subtask.getEpicId());
        updateStatus(epic);
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();

    }

    private void updateStatus(Epic epic) {
        int checkerNew = 1;
        int checkerDone = 1;
        for (Integer id : epic.getSubtasksOfEpic()) {
            StatusTask statusTask = subTasks.get(id).getStatus();
            if (!StatusTask.NEW.equals(statusTask)) {
                checkerNew = 0;
            } else if (!StatusTask.DONE.equals(statusTask)) {
                checkerDone = 0;
            }
        }
        if (checkerNew == 1) {
            epic.setStatus(StatusTask.NEW);
        } else if (checkerDone == 1) {
            epic.setStatus(StatusTask.DONE);
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }
    }
}
