import entity.Epic;
import entity.SimpleTask;
import entity.Subtask;
import manager.InMemoryTaskManager;
import entity.StatusTask;

/*
public class Main {


    public static void main(String[] args) {

        SimpleTask simpleTask = new SimpleTask(1, "Переезд", "111", StatusTask.NEW);
        SimpleTask simpleTask1 = new SimpleTask(0, "Переезд1", "111", StatusTask.NEW);

        Epic epic = new Epic(3, "dataClass.Epic", "222");
        Subtask subtask1 = new Subtask(0, "Subtask1", "333", StatusTask.DONE, 3);
        Subtask subtask2 = new Subtask(0, "Subtask2", "333", StatusTask.NEW, 3);
        Subtask subtask3 = new Subtask(0, "Subtask3", "333", StatusTask.NEW, 3);

        Epic epicWithoutSubtasks = new Epic(7, "Epic1", "222");

        InMemoryTaskManager manager = new InMemoryTaskManager();


        manager.addSimpleTask(simpleTask);
        manager.addSimpleTask(simpleTask1);

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        manager.addEpic(epicWithoutSubtasks);

        manager.getSimple(1);
        manager.getSimple(2);
        manager.getEpic(3);
        manager.getSubtask(5);
        manager.getSimple(1);
        manager.getEpic(7);
        manager.getEpic(3);
        manager.getSubtask(4);

        // Печать истории после каждого запроса
        System.out.println(manager.getHistory());

        // Удаление задачи из истории
        manager.removeSimpleTask(1);

        // Печать истории после удаления задачи
        System.out.println(manager.getHistory());

        // Удаление эпика с подзадачами
        manager.removeEpicTask(3);

        // Печать истории после удаления эпика
        System.out.println(manager.getHistory());
    }
}

     */
