package manager;

import entity.Epic;
import entity.SimpleTask;
import entity.Subtask;

import java.util.List;

public interface TaskManager {


        public List<SimpleTask> showSimpleTask();

        public List<Epic> getEpicTasks(); // Получение списка всех задач.

        public List<Subtask> showSubTasks();

        public SimpleTask getSimple(int simpleId);

        public Epic getEpic(int epicId); // Получение по идентификатору.

        public Subtask getSubtask(int subTaskId);

        public List<Subtask> getSubtasksEpic(int epicId);


        public void deleteAllSimpleTasks();

        public void deletionAllEpic(); // Удаление всех задач.

        public void deletionAllSubtask();

        public void removeSimpleTask(int simpleTaskId);

        public void removeEpicTask(int epicId); // удаление задачи по идентификатору.

        public void removeSubtask(int id);

        public void addSimpleTask(SimpleTask task); // Создание обычных задач

        public void updateSimpleTask(SimpleTask task); // Обновление обычных задач

        public void addEpic(Epic epic); // Создание Сложных задач

        public void updateEpic(Epic epic); // Обновление Сложных задач

        public void addSubtask(Subtask subtask); // Создание  подзадач

        public void updateSubtask(Subtask subtask); // Обновление подзадач

}
