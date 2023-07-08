package manager;

import api.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Epic;
import entity.SimpleTask;
import entity.Subtask;
import entity.Task;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private static Gson gson = new Gson();
    private KVTaskClient kvTaskClient;
    private static String key;

    public HttpTaskManager(String uri) throws IOException, InterruptedException {
        super(null);
        kvTaskClient = new KVTaskClient(URI.create(uri));
    }

    public KVTaskClient getKvTaskClient() {
        return kvTaskClient;
    }

    @Override
    public void save() {
        String taskManagerInString = "";
        for (SimpleTask task : getSimpleTasks().values()) {
            taskManagerInString += gson.toJson(task) + "\n";
        }
        for (Epic epic : getEpicTasks().values()) {
            taskManagerInString += gson.toJson(epic) + "\n";
        }
        for (Subtask subtask : getSubTasks().values()) {
            taskManagerInString += gson.toJson(subtask) + "\n";
        }

        taskManagerInString += "\n" + historyToString(getHistoryManager());

        try {
            kvTaskClient.put(kvTaskClient.getAPI_TOKEN(), taskManagerInString);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public HttpTaskManager load(String key) {
        HttpTaskManager.key = key;
        try {
            HttpTaskManager httpTaskManager = new HttpTaskManager("http://localhost:" + 8000);
            String taskManagerInString = httpTaskManager.getKvTaskClient().load(key);
            if (!taskManagerInString.equals("")) {
                String[] tasks = taskManagerInString.split("\n");
                int curId = 1;
                for (int i = 0; i < tasks.length - 1; i++) {
                    if (!tasks[i].equals("")) {
                        Task task = fromString(tasks[i]);
                        if (task != null && curId < task.getId()) {
                            curId = task.getId();
                        }
                        HttpTaskManager.addTaskWithoutSaving(httpTaskManager, task);
                        httpTaskManager.setNextId(curId + 1);
                    }
                    for (Subtask subtask : httpTaskManager.getSubTasks().values()) {
                        httpTaskManager.getEpicTasks().get(subtask.getEpicId()).getSubtasksOfEpic().add(subtask.getId());
                    }
                }

                String historyInString = tasks[tasks.length - 1];
                List<Integer> listOfIds = historyFromString(historyInString);
                for (Integer id : listOfIds) {
                    if (httpTaskManager.getSimpleTasks().containsKey(id)) {
                        httpTaskManager.getHistoryManager().add(httpTaskManager.getSimpleTasks().get(id));
                    } else if (httpTaskManager.getEpicTasks().containsKey(id)) {
                        httpTaskManager.getHistoryManager().add(httpTaskManager.getEpicTasks().get(id));
                    } else {
                        httpTaskManager.getHistoryManager().add(httpTaskManager.getSubTasks().get(id));
                    }
                }
                return httpTaskManager;
            } else {
                System.out.println("Менеджер на сервере пуст");
                return null;
            }
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    public static Task fromString(String value) throws IOException {
        JsonElement jsonElement = JsonParser.parseString(value);
        if (!jsonElement.isJsonObject()) {
            throw new IOException("Неверный формат Json");
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.toString().contains("epicId")) {
            Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
            return subtask;
        } else if (jsonObject.toString().contains("subtasksIds")) {
            Epic epic = gson.fromJson(jsonObject, Epic.class);
            return epic;
        } else {
            SimpleTask task = gson.fromJson(jsonObject, SimpleTask.class);
            return task;
        }
    }
}
