package manager;

import entity.*;

import java.io.*;
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

        SimpleTask task1 = new SimpleTask(1, "Task1", "Description Task1", StatusTask.NEW);
        SimpleTask task2 = new SimpleTask(2, "Task2", "Description Task2", StatusTask.IN_PROGRESS);
        Epic epic1 = new Epic(1, "Epic1", "Description Epic1");
        Subtask subtask1 = new Subtask(1, "Subtask1", "Description Subtask1", StatusTask.DONE, 1);

        // Добавление задач, эпиков и подзадач в исходный менеджер
        fileBackedTasksManager.addSimpleTask(task1);
        fileBackedTasksManager.addSimpleTask(task2);
        fileBackedTasksManager.addEpic(epic1);
        fileBackedTasksManager.addSubtask(subtask1);

        // Получение некоторых задач для заполнения истории просмотра
        fileBackedTasksManager.getSimple(1);
        fileBackedTasksManager.getEpic(1);
        fileBackedTasksManager.getSubtask(1);

        fileBackedTasksManager.save();

        FileBackedTasksManager newManager = new FileBackedTasksManager(saveFile);
        newManager.loadFromFile(saveFile);

    }

    public void save() throws ManagerSaveException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(saveFile))) {
            // Сохранение задач

            for (SimpleTask simpleTask : showSimpleTask()) {
                writer.println(simpleTask.toString());
            }
            for (Epic epic : getEpicTasks()) {
                writer.println(epic.toString());
                for (Subtask subtask : getSubtasksEpic(epic.getId())) {
                    writer.println(subtask.toString());
                }
            }

            // Сохранение истории
            writer.write(historyToString(historyManager));


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
        if (task instanceof SimpleTask) {
            SimpleTask simpleTask = (SimpleTask) task;
            return TaskType.TASK + "," + simpleTask.getId() + "," + simpleTask.getTitle() + "," +
                    simpleTask.getDescription() + "," + simpleTask.getStatus();
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            // Возвращаем строку, состоящую из типа задачи, ID, заголовка и описания
            return TaskType.EPIC + "," + epic.getId() + "," + epic.getTitle() + "," + epic.getDescription();
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            // Возвращаем строку, состоящую из типа задачи, ID, заголовка, описания, статуса и ID эпика
            return TaskType.SUBTASK + "," + subtask.getId() + "," + subtask.getTitle() + "," +
                    subtask.getDescription() + "," + subtask.getStatus() + "," + subtask.getEpicId();
        }
        return null;
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        TaskType taskType = TaskType.valueOf(values[0]);

        switch (taskType) {
            case TASK:
                int taskId = Integer.parseInt(values[1]);
                String taskTitle = values[2];
                String taskDescription = values[3];
                StatusTask taskStatus = StatusTask.valueOf(values[4]);
                // Создаем и возвращаем новую простую задачу
                return new SimpleTask(taskId, taskTitle, taskDescription, taskStatus);
            case EPIC:
                // Если тип задачи - эпик
                int epicId = Integer.parseInt(values[1]);
                String epicTitle = values[2];
                String epicDescription = values[3];
                // Создаем и возвращаем новый эпик
                return new Epic(epicId, epicTitle, epicDescription);
            case SUBTASK:
                // Если тип задачи - подзадача
                int subtaskId = Integer.parseInt(values[1]);
                String subtaskTitle = values[2];
                String subtaskDescription = values[3];
                StatusTask subtaskStatus = StatusTask.valueOf(values[4]);
                int epicIdOfSubtask = Integer.parseInt(values[5]);
                // Создаем и возвращаем новую подзадачу
                return new Subtask(subtaskId, subtaskTitle, subtaskDescription, subtaskStatus, epicIdOfSubtask);
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

    public void loadFromFile(File file) throws ManagerSaveException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = fromString(line);
                if (task != null) {
                    if (task instanceof SimpleTask) {
                        addSimpleTask((SimpleTask) task);
                    } else if (task instanceof Epic) {
                        addEpic((Epic) task);
                    } else if (task instanceof Subtask) {
                        addSubtask((Subtask) task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных из файла.", e);
        }
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

