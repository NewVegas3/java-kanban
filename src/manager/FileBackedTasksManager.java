package manager;

import entity.*;
import exceptions.ManagerSaveException;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;



public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File saveFile;

    public FileBackedTasksManager(File saveFile) {
        this.saveFile = saveFile;
    }

    public static void main(String[] args) {
        File saveFile = new File("./resources/saveFile.csv");

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(saveFile);

        SimpleTask task1 = new SimpleTask(1, "Task1", "Description Task1", StatusTask.NEW, 30,
                Instant.ofEpochSecond(1656136873));
        SimpleTask task2 = new SimpleTask(2, "Task2", "Description Task2", StatusTask.IN_PROGRESS,160,
                Instant.ofEpochSecond(1656144073));
        Epic epic1 = new Epic(1, "Epic1", "Description Epic1");
        Subtask subtask1 = new Subtask(1, "Subtask1", "Description Subtask1", StatusTask.DONE, 3,15,
                Instant.ofEpochSecond(1677780600));

        // Добавление задач, эпиков и подзадач в исходный менеджер
        fileBackedTasksManager.addSimpleTask(task1);
        fileBackedTasksManager.addSimpleTask(task2);
        fileBackedTasksManager.addEpic(epic1);
        fileBackedTasksManager.addSubtask(subtask1);

        // Получение некоторых задач для заполнения истории просмотра
        fileBackedTasksManager.getSimple(1);
        fileBackedTasksManager.getEpic(3);
        fileBackedTasksManager.getSubtask(4);

        FileBackedTasksManager newManager = new FileBackedTasksManager(saveFile);
        newManager.loadFromFile(saveFile);
    }

    public void save() throws ManagerSaveException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(saveFile))) {
            // Сохранение задач

            for (SimpleTask simpleTask : showSimpleTasks()) {
                writer.println(toString(simpleTask));
            }
            for (Epic epic : showEpicTasks()) {
                writer.println(toString(epic));
            }
            for (Subtask subtask : showSubTasks()) {
                    writer.println(toString(subtask));
            }

            // Сохранение истории
            writer.write('\n' + historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач.", e);
        }
    }

    private static String historyToString(HistoryManager manager) {
        List<String> newHistory = new ArrayList<>();

        for (Task task : manager.getHistory()) {
            newHistory.add(String.valueOf(task.getId()));
        }
        return String.join("," ,newHistory);
    }

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        if (task instanceof SimpleTask) {
            SimpleTask simpleTask = (SimpleTask) task;
            sb.append(simpleTask.getId()).append(",")
                    .append(TaskType.TASK).append(",")
                    .append(simpleTask.getTitle()).append(",")
                    .append(simpleTask.getStatus()).append(",")
                    .append(simpleTask.getDescription()).append(",")
                    .append(simpleTask.getDuration()).append(",")
                    .append(simpleTask.getStartTime());
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            sb.append(epic.getId()).append(",")
                    .append(TaskType.EPIC).append(",")
                    .append(epic.getTitle()).append(",")
                    .append(epic.getStatus()).append(",")
                    .append(epic.getDescription()).append(",")
                    .append(epic.getDuration()).append(",")
                    .append(epic.getStartTime());
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            sb.append(subtask.getId()).append(",")
                    .append(TaskType.SUBTASK).append(",")
                    .append(subtask.getTitle()).append(",")
                    .append(subtask.getStatus()).append(",")
                    .append(subtask.getDescription()).append(",")
                    .append(subtask.getEpicId()).append(",")
                    .append(subtask.getDuration()).append(",")
                    .append(subtask.getStartTime());
        }
        return sb.toString();
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        TaskType taskType = TaskType.valueOf(values[1]);

        switch (taskType) {
            case TASK:
                int taskId = Integer.parseInt(values[0]);
                String taskTitle = values[2];
                String taskDescription = values[4];
                StatusTask taskStatus = StatusTask.valueOf(values[3]);
                long taskDuration = Long.parseLong(values[5]); //  values[5] вместо value[5]
                Instant taskStartTime = Instant.parse((values[6]).toString()); //  values[6] вместо value[6]

                // Создаем и возвращаем новую простую задачу
                return new SimpleTask(taskId, taskTitle, taskDescription, taskStatus, taskDuration, taskStartTime);
            case EPIC:
                // Если тип задачи - эпик
                int epicId = Integer.parseInt(values[0]);
                String epicTitle = values[2];
                String epicDescription = values[4];

                return new Epic(epicId, epicTitle, epicDescription);
            case SUBTASK:
                // Если тип задачи - подзадача
                int subtaskId = Integer.parseInt(values[0]);
                String subtaskTitle = values[2];
                String subtaskDescription = values[4];
                StatusTask subtaskStatus = StatusTask.valueOf(values[3]);
                int epicIdOfSubtask = Integer.parseInt(values[5]);
                long subtaskDuration = Long.parseLong(values[6]); //  values[6] вместо value[6]
                Instant subtaskStartTime = Instant.parse((values[7]).toString()); //  values[7] вместо value[7]

                return new Subtask(subtaskId, subtaskTitle, subtaskDescription, subtaskStatus, epicIdOfSubtask,
                        subtaskDuration, subtaskStartTime);
        }

        return null;
    }


    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] ids = value.split(",");
        for (String id : ids) {
            // Преобразуем каждый ID из строки в целочисленное значение и добавляем в список истории
            history.add(Integer.parseInt(id));
        }
        return history;
    }

    public FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        List<String> tasksArray = new ArrayList<>();
        String history = "";
        FileBackedTasksManager manager = new FileBackedTasksManager(file.toPath().toFile());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null && !line.equals("")) {
                Task task = fromString(line);
                if (task != null) {
                    if (task instanceof SimpleTask) {
                        getSimpleTasks().put(task.getId(), (SimpleTask) task);
                    } else if (task instanceof Epic) {
                        getEpicTasks().put(task.getId(), (Epic) task);
                    } else if (task instanceof Subtask) {
                        getSubTasks().put(task.getId(), (Subtask) task);
                    }
                }
            }
            while (reader.ready()) {
                line = reader.readLine();
                if (!line.equals("")) {
                    tasksArray.add(line);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных из файла.", e);
        }
        tasksArray.remove(0);
        if (!tasksArray.isEmpty()) {
            List<Integer> listOfIds = historyFromString(history);
            for (Integer id : listOfIds) { // проверка наличия задачи в одной из трех мап и добавление в историю
                if (manager.getSimpleTasks().containsKey(id)) {
                    manager.getHistory().add(manager.showSimpleTasks().get(id));
                } else if (manager.getEpicTasks().containsKey(id)) {
                    manager.getHistory().add(manager.getEpicTasks().get(id));
                } else {
                    manager.getHistory().add(manager.getSubTasks().get(id));
                }
            }
        }
        return manager;
    }


    @Override
    public void addSimpleTask(SimpleTask task) {
        super.addSimpleTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public SimpleTask getSimple(int simpleId) {
        SimpleTask sim = super.getSimple(simpleId);
        save();
        return sim;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic ep = super.getEpic(epicId);
        save();
        return ep;
    }

    @Override
    public Subtask getSubtask(int subTaskId) {
        Subtask sub = super.getSubtask(subTaskId);
        save();
        return sub;
    }

    @Override
    public void deleteAllSimpleTasks() {
        super.deleteAllSimpleTasks();
        save();
    }

    @Override
    public void deletionAllEpic() {
        super.deletionAllEpic();
        save();
    }

    @Override
    public void deletionAllSubtask() {
        super.deletionAllSubtask();
        save();
    }

    @Override
    public void removeSimpleTask(int simpleTaskId) {
        super.removeSimpleTask(simpleTaskId);
        save();
    }

    @Override
    public void removeEpicTask(int epicId) {
        super.removeEpicTask(epicId);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void updateSimpleTask(SimpleTask task) {
        super.updateSimpleTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}

