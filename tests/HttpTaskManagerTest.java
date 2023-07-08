import api.HttpTaskServer;
import api.KVServer;
import com.google.gson.Gson;
import entity.Epic;
import entity.SimpleTask;
import entity.StatusTask;
import entity.Subtask;
import manager.HttpTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.time.Instant;

public class HttpTaskManagerTest {

    HttpTaskManager taskManager;

    KVServer kvServer;

    HttpTaskServer taskServer;

    private Gson gson;

    SimpleTask task1;
    Epic epic1;
    Subtask subtask1;

    @BeforeEach
    public void start() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            taskManager = new HttpTaskManager("http://localhost:8000");
            taskServer = new HttpTaskServer(taskManager);
            taskServer.start();
            gson = new Gson();

            task1 = new SimpleTask(1, "Task1", "Description Task1", StatusTask.NEW, 30,
                    Instant.ofEpochSecond(1656136873));
            epic1 = new Epic(1, "Epic1", "Description Epic1");
            subtask1 = new Subtask(1, "Subtask1", "Description Subtask1", StatusTask.DONE, 3,15,
                    Instant.ofEpochSecond(1677780600));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void end() {
        kvServer.stop();
        taskServer.stop();
    }

}
