import entity.Epic;
import entity.SimpleTask;
import entity.StatusTask;
import entity.Subtask;
import exceptions.SaveException;
import manager.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @TempDir
    Path tempDir;
    File file;

    @BeforeEach
    public void setUp() throws IOException {
        try {
            file = tempDir.resolve("saveFile.csv").toFile();
        } catch (InvalidPathException ipe) {
            System.err.println(
                    "Error creating temporary test file in " +
                            this.getClass().getSimpleName());
            return;
        }
        {
            super.taskManager = new FileBackedTasksManager(file);
        }
    }

        @Test
        public void loadFromFile () {
            File saveFile = new File("saveFile.csv");

            FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(saveFile);

            SimpleTask task1 = new SimpleTask(1, "Task1", "Description Task1", StatusTask.NEW, 30,
                    Instant.ofEpochSecond(1656136873));
            SimpleTask task2 = new SimpleTask(2, "Task2", "Description Task2", StatusTask.IN_PROGRESS, 160,
                    Instant.ofEpochSecond(1656144073));
            Epic epic1 = new Epic(1, "Epic1", "Description Epic1");
            Subtask subtask1 = new Subtask(1, "Subtask1", "Description Subtask1", StatusTask.DONE, 3, 15,
                    Instant.ofEpochSecond(1677780600));

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

            assertEquals(2, fileBackedTasksManager.showSimpleTasks().size());
        }

        @Test
        void save () {
            SimpleTask task1 = new SimpleTask(1, "Task1", "Description Task1", StatusTask.NEW, 30,
                    Instant.ofEpochSecond(1656136873));
            SimpleTask task2 = new SimpleTask(2, "Task2", "Description Task2", StatusTask.IN_PROGRESS, 160,
                    Instant.ofEpochSecond(1656144073));
            Epic epic1 = new Epic(1, "Epic1", "Description Epic1");
            Subtask subtask1 = new Subtask(1, "Subtask1", "Description Subtask1", StatusTask.DONE, 3, 15,
                    Instant.ofEpochSecond(1677780600));

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
                    "1,TASK,Task1,NEW,Description Task1,30,2022-06-25T06:01:13Z",
                    "2,TASK,Task2,IN_PROGRESS,Description Task2,160,2022-06-25T08:01:13Z",
                    "3,EPIC,Epic1,DONE,Description Epic1,0,2023-03-02T18:10:00Z",
                    "4,SUBTASK,Subtask1,DONE,Description Subtask1,3,15,2023-03-02T18:10:00Z"
            );

            assertEquals(savedManager, tasksArray, "Ошибка сохранения задач");
        }
    }



