package manager;

import dataClass.Epic;
import dataClass.SimpleTask;
import dataClass.Subtask;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private int nextId = 1;
    private HashMap<Integer, SimpleTask> simpleTasks = new HashMap<>();
    private HashMap<Integer, Epic> epicTasks = new HashMap<>();
    private HashMap<Integer, Subtask> subTasks = new HashMap<>();

    public ArrayList<SimpleTask> showSimpleTask() {
        return new ArrayList<>(simpleTasks.values());
    }

    public ArrayList<Epic> getEpicTasks() { // Получение списка всех задач.
        return new ArrayList<>(epicTasks.values());
    }

    public ArrayList<Subtask> showSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public SimpleTask getSimple(int simpleId) {
        return simpleTasks.get(simpleId);
    }

    public Epic getEpic(int epicId) {
        return epicTasks.get(epicId);
    }

    public Subtask getSubtask(int subTaskId) {
        return subTasks.get(subTaskId);
    }

    public ArrayList<Subtask> getSubtasksEpic(int epicId) {
        Epic epic = epicTasks.get(epicId);
        ArrayList<Subtask> subtaskOfEpic = new ArrayList<>();
        for (Integer id : epic.getSubtasksOfEpic()) {
            subtaskOfEpic.add(subTasks.get(id));
        }
        return subtaskOfEpic;
    }


    public void deleteAllSimpleTasks() {
        simpleTasks.clear();
    }

    public void deletionAllEpic() {
        epicTasks.clear();
        subTasks.clear();
    }

    public void deletionAllSubtask() {
        subTasks.clear();
        for (Epic epic : epicTasks.values()) {
            epic.getSubtasksOfEpic().clear();
            updateStatus(epic);
        }
    }

    public void removeSimpleTask(int simpleTaskId) {
        simpleTasks.remove(simpleTaskId);
    }

    public void removeEpicTask(int epicId) {
        Epic epic = epicTasks.get(epicId);
        for (Integer id : epic.getSubtasksOfEpic()) {
            subTasks.remove(id);
        }
        epicTasks.remove(epicId);
    }

    public void removeSubtask(int id) {
        int epicId = subTasks.get(id).getEpicId();
        Epic epic = epicTasks.get(epicId);
        epicTasks.get(epicId).getSubtasksOfEpic().remove(Integer.valueOf(id)); // очень надеюсь что правильно...
        updateStatus(epic);
        subTasks.remove(id);
    }

    public void addSimpleTask(SimpleTask task) {
        task.setId(nextId);
        simpleTasks.put(task.getId(), task);
        nextId++;
    }

    public void updateSimpleTask(SimpleTask task) {
        simpleTasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(nextId);
        epicTasks.put(epic.getId(), epic);
        nextId++;
    }

    private void updateEpic(Epic epic) {
        epicTasks.put(epic.getId(), epic);
    }

    public void updateStatus(Epic epic) {
        int checkerNew = 1;
        int checkerDone = 1;
        for (Integer id : epic.getSubtasksOfEpic()) {
            if (!subTasks.get(id).getStatus().equals("NEW")) {
                checkerNew = 0;
            } else if (!subTasks.get(id).getStatus().equals("DONE")) {
                checkerDone = 0;
            }
        }
        if (checkerNew == 1) {
            epic.setStatus("NEW");
        } else if (checkerDone == 1) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }

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

    public void updateSubtask(Subtask subtask) {
        subTasks.put(subtask.getId(), subtask);
        Epic epic = epicTasks.get(subtask.getEpicId());
        updateStatus(epic);
    }
}
