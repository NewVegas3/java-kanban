
import entity.Epic;
import entity.SimpleTask;
import entity.StatusTask;
import entity.Subtask;
import exceptions.SaveException;
import manager.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    {
        super.taskManager = new FileBackedTasksManager(new File("./resources/saveFile.csv"));
    }

    File file;

    @BeforeEach
    public void x() {
        file = new File("./resources/saveFile.csv");
    }

    @Test
    public void loadFromFile() {
        File saveFile = new File("./resources/saveFile.csv");

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(saveFile);

        SimpleTask task1 = new SimpleTask(1, "Task1", "Description Task1", StatusTask.NEW, 30,
                Instant.ofEpochSecond(1656136873));
        SimpleTask task2 = new SimpleTask(2, "Task2", "Description Task2", StatusTask.IN_PROGRESS,160,
                Instant.ofEpochSecond(1656144073));
        Epic epic1 = new Epic(1, "Epic1", "Description Epic1");
        Subtask subtask1 = new Subtask(1, "Subtask1", "Description Subtask1", StatusTask.DONE, 3,15,
                Instant.ofEpochSecond(1656144073));

        // Добавление задач, эпиков и подзадач в исходный менеджер
        fileBackedTasksManager.addSimpleTask(task1);
        fileBackedTasksManager.addSimpleTask(task2);
        fileBackedTasksManager.addEpic(epic1);
        fileBackedTasksManager.addSubtask(subtask1);

        // Получение некоторых задач для заполнения истории просмотра
        fileBackedTasksManager.getSimple(1);
        fileBackedTasksManager.getEpic(1);
        fileBackedTasksManager.getSubtask(1);

        FileBackedTasksManager newManager = new FileBackedTasksManager(saveFile);
        newManager.loadFromFile(saveFile);

        assertEquals(2,fileBackedTasksManager.showSimpleTasks().size());
    }

    @Test
    void save() {
        SimpleTask task1 = new SimpleTask(1, "Task1", "Description Task1", StatusTask.NEW, 30,
                Instant.ofEpochSecond(1656136873));
        SimpleTask task2 = new SimpleTask(2, "Task2", "Description Task2", StatusTask.IN_PROGRESS, 160,
                Instant.ofEpochSecond(1656144073));
        Epic epic1 = new Epic(1, "Epic1", "Description Epic1");
        Subtask subtask1 = new Subtask(1, "Subtask1", "Description Subtask1", StatusTask.DONE, 3, 15,
                Instant.ofEpochSecond(1656144073));

        taskManager.addSimpleTask(task1);
        taskManager.addSimpleTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);

        List<String> tasksArray = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (!line.equals("")) {
                    tasksArray.add(line);
                }
            }
        } catch (IOException exception) {
            throw new SaveException("Ошибка чтения файла");
        }

        List<String> savedManager = List.of(
                "1,SimpleTask,Task1,NEW,30,1656136873,Description Task1",
                "2,SimpleTask,Task2,IN_PROGRESS,160,1656144073,Description Task2",
                "1,Epic,Epic1,NEW,0,0,Description Epic1",
                "1,Subtask,Subtask1,DONE,3,1656144073,Description Subtask1"
        );

        assertEquals(savedManager, tasksArray, "Ошибка сохранения задач");
    }


}
