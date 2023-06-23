package manager;

import entity.*;
import exceptions.SameTimeException;

import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected int nextId = 1;
    protected Map<Integer, SimpleTask> simpleTasks = new HashMap<>();
    protected Map<Integer, Epic> epicTasks = new HashMap<>();
    protected Map<Integer, Subtask> subTasks = new HashMap<>();

    Comparator<Task> comparator = new StartTimeComparator().thenComparing(new IdComparator());

    private final Set<Task> tasksWithPriority = new TreeSet<>(comparator);

    public Map<Integer, SimpleTask> getSimpleTasks() {
        return simpleTasks;
    }

    public Map<Integer, Epic> getEpicTasks() {
        return epicTasks;
    }

    public Map<Integer, Subtask> getSubTasks() {
        return subTasks;
    }

    @Override
    public List<SimpleTask> showSimpleTasks() {
        return new ArrayList<>(simpleTasks.values());
    }

    @Override
    public List<Epic> showEpicTasks() {
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

    protected void updateStatus(Epic epic) {
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

    @Override
    public void setStartAndEndTimeToEpic(int id) {
        Epic epic = epicTasks.get(id);
        if (epic != null) {
            ArrayList<Integer> subtasksIds = epic.getSubtasksOfEpic();
            if (epic.getStartTime() == Instant.ofEpochSecond(0)) {
                epic.setStartTime(Instant.MAX);
            }
            Instant epicStartTime = epic.getStartTime();
            long totalTime = 0;

            if (!subtasksIds.isEmpty()) {
                for (Integer subtaskId : subtasksIds) {
                    Instant subtaskStartTime = subTasks.get(subtaskId).getStartTime();
                    long subtaskDuration = subTasks.get(subtaskId).getDuration();
                    if (subtaskStartTime.isBefore(epicStartTime)) {
                        epicStartTime = subtaskStartTime;
                    }
                    totalTime += subtaskDuration;
                }
                epic.setStartTime(epicStartTime);
                epic.setEndTime(epic.getStartTime().plusSeconds(totalTime));
            } else {
                epic.setStartTime(null);
                epic.setEndTime(null);
                epic.setDuration(0);
            }
        }
    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(tasksWithPriority);
        // эпики не попадают в список, так как у них нет собственного времени начала и конца
    }

    private void addToTasksWithPriority(Task task) {
        checkTimeIntersections(task);
        tasksWithPriority.add(task);
    }

    private void checkTimeIntersections(Task task) {
        List<Task> tasksWithPriority = getPrioritizedTasks();
        for (int i = 1; i < tasksWithPriority.size(); i++) {
            if (task.getStartTime().isBefore(tasksWithPriority.get(i).getEndTime()) ||
                    tasksWithPriority.get(i).getStartTime().isAfter(task.getEndTime())) {
                throw new SameTimeException("Задачи пересекаются "
                        + task.getId()
                        + " и "
                        + tasksWithPriority.get(i).getId());
            }
        }
    }

    public class StartTimeComparator implements Comparator<Task> {
        // компаратор для сравнения времени начала задач
        @Override
        public int compare(Task task1, Task task2) {
            return task1.getStartTime().compareTo(task2.getStartTime());
        }
    }

    public class IdComparator implements Comparator<Task> {
        // компаратор для сравнения задач по id
        @Override
        public int compare(Task task1, Task task2) {
            return task1.getId() - task2.getId();
        }
    }
}
