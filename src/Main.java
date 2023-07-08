import api.KVServer;
import com.google.gson.Gson;
import entity.Epic;
import entity.SimpleTask;
import entity.StatusTask;
import entity.Subtask;
import manager.HttpTaskManager;
import manager.TaskManager;

import java.io.IOException;
import java.time.Instant;

public class Main {

    Gson gson = new Gson();

    public static void main(String[] args) throws IOException, InterruptedException {
        SimpleTask task1 = new SimpleTask(1, "Task1", "Description Task1", StatusTask.NEW, 30,
                Instant.ofEpochSecond(1656136873));
        SimpleTask task2 = new SimpleTask(2, "Task2", "Description Task2", StatusTask.IN_PROGRESS,160,
                Instant.ofEpochSecond(1656144073));
        Epic epic1 = new Epic(1, "Epic1", "Description Epic1");
        Subtask subtask1 = new Subtask(1, "Subtask1", "Description Subtask1", StatusTask.DONE, 3,15,
                Instant.ofEpochSecond(1677780600));
        Epic epicWithoutSubtasks = new Epic(7, "Epic1", "222");
        KVServer kvServer = new KVServer();
        kvServer.start();
        TaskManager manager = new HttpTaskManager("http://localhost:8000");
        manager.addSimpleTask(task1);
        manager.addSimpleTask(task2);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addEpic(epicWithoutSubtasks);
        manager.getSimple(1);
        manager.getSimple(2);
        manager.getEpic(3);
        manager.getSubtask(4);
        manager.getSimple(1);
        manager.getEpic(5);
        manager.getEpic(3);
        manager.getSubtask(4);
        // Печать истории после каждого запроса
        System.out.println(manager.getHistory());
        HttpTaskManager newManager = ((HttpTaskManager) manager).load(((HttpTaskManager) manager).getKvTaskClient().getAPI_TOKEN());
        System.out.println(newManager.getHistory());
        System.out.println(newManager.getPrioritizedTasks());
        kvServer.stop();
    }
}


