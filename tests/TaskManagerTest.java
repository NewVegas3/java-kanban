import entity.*;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    //protected InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addSimpleTask() {
        SimpleTask task =  new SimpleTask(1, "Task1", "Description Task1", StatusTask.NEW,30,
                Instant.ofEpochSecond(1656136873));
        taskManager.addSimpleTask(task);

        final Task savedTask = taskManager.getSimple(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<SimpleTask> tasks = taskManager.showSimpleTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addSubtask() {
        Subtask subtask = new Subtask(1, "Subtask1", "Description Subtask1", StatusTask.NEW,1,25,
                Instant.ofEpochSecond(1687266000));
        taskManager.addSubtask(subtask);

        Epic epic = new Epic(1, "Epic1", "Description Epic1");
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        final Subtask saveSubtask = taskManager.getSubtask(2);

        assertNotNull(saveSubtask, "Задача не найдена.");
        assertEquals(subtask,saveSubtask,"Задача не совпадает.");

        final List<Subtask> subtasks = taskManager.showSubTasks();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");

    }

    @Test
    void addEpic() {
        Epic epic1 = new Epic(1, "Epic1", "Description Epic1");
        taskManager.addEpic(epic1);

        final Task savedEpic = taskManager.getEpic(epic1.getId());

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic1, savedEpic, "Задачи не совпадают.");

        final List<Epic> epicTasks = taskManager.showEpicTasks();

        assertNotNull(epicTasks, "Задачи на возвращаются.");
        assertEquals(1, epicTasks.size(), "Неверное количество задач.");
        assertEquals(epic1, epicTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void showSimpleTask() {
        SimpleTask task1 = new SimpleTask(1, "Задача1", "Описание задачи 1", StatusTask.NEW,25,
                Instant.ofEpochSecond(1687266000));
        SimpleTask task2 = new SimpleTask(2, "Задача2", "Описание задачи 2", StatusTask.IN_PROGRESS,15,
                Instant.ofEpochSecond(1656144073));
        taskManager.addSimpleTask(task1);
        taskManager.addSimpleTask(task2);

        final List<SimpleTask> tasks = taskManager.showSimpleTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertTrue(tasks.contains(task1), "Задача 1 отсутствует в списке.");
        assertTrue(tasks.contains(task2), "Задача 2 отсутствует в списке.");
    }

    @Test
    void showEpicTasks() {
        Epic epic = new Epic(1, "Эпик1", "Описание эпика 1");
        taskManager.addEpic(epic);

        final List<Epic> epics = taskManager.showEpicTasks();

        assertNotNull(epics, "Подзадачи не возвращаются.");
        //assertTrue(epics.isEmpty(), "Неверное количество подзадач.");
        assertEquals(1,epics.size(),"Неверное количество задач.");
    }

    @Test
    void showSubTasks() {
        Subtask subtask = new Subtask(1, "Subtask1", "Description Subtask1", StatusTask.NEW,1,15,
                Instant.ofEpochSecond(1656144073));
        taskManager.addSubtask(subtask);

        Epic epic = new Epic(1, "Epic1", "Description Epic1");
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

        final List<Subtask> subtasks = taskManager.showSubTasks();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
    }

    @Test
    void getSimple() {
        SimpleTask task = new SimpleTask(1, "Задача1", "Описание задачи 1", StatusTask.NEW,30,
                Instant.ofEpochSecond(1656144073));
        taskManager.addSimpleTask(task);

        final Task savedTask = taskManager.getSimple(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void getEpic() {
        Epic epic = new Epic(1, "Эпик1", "Описание эпика 1");
        taskManager.addEpic(epic);

        final Task savedEpic = taskManager.getEpic(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void getSubtask() {
        Subtask subtask = new Subtask(1, "Subtask1", "Description Subtask1", StatusTask.NEW,1,30,
                Instant.ofEpochSecond(1656144073));
        taskManager.addSubtask(subtask);

        Epic epic = new Epic(1, "Epic1", "Description Epic1");
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

        final Task savedSubtask = taskManager.getSubtask(subtask.getId());

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void getSubtasksEpic() {
        Epic epic = new Epic(1, "Эпик1", "Описание эпика 1");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask(1, "Подзадача1", "Описание подзадачи 1", StatusTask.NEW, 1,15,
                Instant.ofEpochSecond(1656144073));
        Subtask subtask2 = new Subtask(2, "Подзадача2", "Описание подзадачи 2", StatusTask.IN_PROGRESS, 1,30,
                Instant.ofEpochSecond(1656144073));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        final List<Subtask> subtasks = taskManager.getSubtasksEpic(epic.getId());

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
        assertTrue(subtasks.contains(subtask1), "Подзадача 1 отсутствует в списке.");
        assertTrue(subtasks.contains(subtask2), "Подзадача 2 отсутствует в списке.");
    }

    @Test
    void deleteAllSimpleTasks() {
        SimpleTask task1 = new SimpleTask(1, "Задача1", "Описание задачи 1", StatusTask.NEW,15,
                Instant.ofEpochSecond(1656144073));
        SimpleTask task2 = new SimpleTask(2, "Задача2", "Описание задачи 2", StatusTask.IN_PROGRESS,30,
                Instant.ofEpochSecond(1656144073));
        taskManager.addSimpleTask(task1);
        taskManager.addSimpleTask(task2);

        taskManager.deleteAllSimpleTasks();

        final List<SimpleTask> tasks = taskManager.showSimpleTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertTrue(tasks.isEmpty(), "Задачи не удалены.");
    }

    @Test
    void deletionAllEpic() {
        Epic epic = new Epic(1, "Эпик1", "Описание эпика 1");
        taskManager.addEpic(epic);

        taskManager.deletionAllEpic();

        final List<Epic> epicTasks = taskManager.showEpicTasks();

        assertNotNull(epicTasks, "Задачи не возвращаются.");
        assertTrue(epicTasks.isEmpty(), "Эпики не удалены.");

        final List<Subtask> subtasks = taskManager.showSubTasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertTrue(subtasks.isEmpty(), "Подзадачи не удалены.");
    }

    @Test
    void deletionAllSubtask() {
        Epic epic = new Epic(1, "Эпик1", "Описание эпика 1");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask(1, "Подзадача1", "Описание подзадачи 1", StatusTask.NEW, 1,15,
                Instant.ofEpochSecond(1656144073));
        Subtask subtask2 = new Subtask(2, "Подзадача2", "Описание подзадачи 2", StatusTask.IN_PROGRESS, 1,30,
                Instant.ofEpochSecond(1656144073));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        taskManager.deletionAllSubtask();
        taskManager.removeEpicTask(epic.getId());

        final List<Subtask> subtasks = taskManager.showSubTasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertTrue(subtasks.isEmpty(), "Подзадачи не удалены.");
    }

    @Test
    void removeSimpleTask() {
        SimpleTask task = new SimpleTask(1, "Задача1", "Описание задачи 1", StatusTask.NEW,15,
                Instant.ofEpochSecond(1656144073));
        taskManager.addSimpleTask(task);

        taskManager.removeSimpleTask(task.getId());

        final List<SimpleTask> tasks = taskManager.showSimpleTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertFalse(tasks.contains(task), "Задача не удалена.");
    }

    @Test
    void removeEpicTask() {
        Epic epic = new Epic(1, "Эпик1", "Описание эпика 1");
        taskManager.addEpic(epic);

        taskManager.removeEpicTask(epic.getId());

        final List<Epic> epicTasks = taskManager.showEpicTasks();

        assertNotNull(epicTasks, "Задачи не возвращаются.");
        assertFalse(epicTasks.contains(epic), "Эпик не удален.");

        final List<Subtask> subtasks = taskManager.showSubTasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertTrue(subtasks.isEmpty(), "Подзадачи не удалены.");
    }

    @Test
    void removeSubtask() {
        Epic epic = new Epic(1, "Эпик1", "Описание эпика 1");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask(1, "Подзадача1", "Описание подзадачи 1", StatusTask.NEW,
                1,15,Instant.ofEpochSecond(1656144073));
        Subtask subtask2 = new Subtask(2, "Подзадача2", "Описание подзадачи 2", StatusTask.IN_PROGRESS,
                1,30,Instant.ofEpochSecond(1656144073));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.removeSubtask(subtask1.getId());
        taskManager.removeSubtask(subtask2.getId());

        final List<Subtask> subtasks = taskManager.showSubTasks();


        assertFalse(subtasks.contains(subtask1), "Подзадача не удалена.");
        assertFalse(subtasks.contains(subtask2), "Подзадача не удалена.");
    }

    @Test
    void updateSimpleTask() {
        SimpleTask task = new SimpleTask(1, "Задача1", "Описание задачи 1", StatusTask.NEW,
                15,Instant.ofEpochSecond(1656144073));
        taskManager.addSimpleTask(task);

        SimpleTask updatedTask = new SimpleTask(1, "Обновленная задача1",
                "Обновленное описание задачи 1", StatusTask.IN_PROGRESS,30,Instant.ofEpochSecond(1656144073));
        taskManager.updateSimpleTask(updatedTask);

        final SimpleTask savedTask = taskManager.getSimple(updatedTask.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(updatedTask, savedTask, "Задачи не совпадают.");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic(1, "Эпик1", "Описание эпика 1");
        taskManager.addEpic(epic);

        Epic updatedEpic = new Epic(1, "Обновленный эпик1", "Обновленное описание эпика 1");
        taskManager.updateEpic(updatedEpic);

        final Epic savedEpic = taskManager.getEpic(updatedEpic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(updatedEpic, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic(1, "Эпик1", "Описание эпика 1");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask(1, "Подзадача1", "Описание подзадачи 1", StatusTask.NEW,
                1,15,Instant.ofEpochSecond(1656144073));
        taskManager.addSubtask(subtask1);

        Subtask updatedSubtask = new Subtask(1, "Обновленная подзадача1", "Обновленное описание подзадачи 1",
                StatusTask.IN_PROGRESS, 1,30,Instant.ofEpochSecond(1656144073));
        taskManager.updateSubtask(updatedSubtask);

        final Subtask savedSubtask = taskManager.getSubtask(updatedSubtask.getId());

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(updatedSubtask, savedSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void getHistory() {
        SimpleTask task = new SimpleTask(1, "Задача1", "Описание задачи 1", StatusTask.NEW,
                30,Instant.ofEpochSecond(1656144073));
        taskManager.addSimpleTask(task);

        task.setStatus(StatusTask.IN_PROGRESS);
        taskManager.updateSimpleTask(task);

        final List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История не возвращается.");
        assertEquals(0, history.size(), "Неверное количество записей в истории.");
        assertTrue(history.isEmpty(), "Подзадачи не удалены.");
    }
}
