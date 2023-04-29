package manager;

import entity.Epic;
import entity.SimpleTask;
import entity.Subtask;
import entity.Task;

import java.util.List;

public interface TaskManager {

         List<SimpleTask> showSimpleTask();

         List<Epic> getEpicTasks(); // Получение списка всех задач.

         List<Subtask> showSubTasks();

         SimpleTask getSimple(int simpleId);

         Epic getEpic(int epicId); // Получение по идентификатору.

         Subtask getSubtask(int subTaskId);

         List<Subtask> getSubtasksEpic(int epicId);

         List<Task> getHistory();

         void deleteAllSimpleTasks();

         void deletionAllEpic(); // Удаление всех задач.

         void deletionAllSubtask();

         void removeSimpleTask(int simpleTaskId);

         void removeEpicTask(int epicId); // удаление задачи по идентификатору.

         void removeSubtask(int id);

         void addSimpleTask(SimpleTask task); // Создание обычных задач

         void updateSimpleTask(SimpleTask task); // Обновление обычных задач

         void addEpic(Epic epic); // Создание Сложных задач

         void updateEpic(Epic epic); // Обновление Сложных задач

         void addSubtask(Subtask subtask); // Создание  подзадач

         void updateSubtask(Subtask subtask); // Обновление подзадач

}
