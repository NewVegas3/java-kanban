import entity.Epic;
import entity.SimpleTask;
import entity.Subtask;
import manager.InMemoryTaskManager;
import manager.StatusTask;

public class Main {


    public static void main(String[] args) {

        SimpleTask simpleTask = new SimpleTask(0, "Переезд", "111",StatusTask.NEW);
        SimpleTask simpleTask1 = new SimpleTask(0, "Переезд1", "111",StatusTask.NEW);

         Epic epic = new Epic(0, "dataClass.Epic", "222", StatusTask.NEW);
         Subtask subtask = new Subtask(0, "Subtask1", "333", StatusTask.DONE, 3);
        Subtask subtask1 = new Subtask(0, "Subtask2", "333", StatusTask.NEW, 3);

        Epic epic1 = new Epic(0, "Epic1", "222", StatusTask.NEW);
        Subtask subtask3 = new Subtask(0, "Subtask3", "333", StatusTask.NEW, 6);

        InMemoryTaskManager manager = new InMemoryTaskManager();


        manager.addSimpleTask(simpleTask);
        manager.addSimpleTask(simpleTask1);

        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.addSubtask(subtask1);

        manager.addEpic(epic1);
        manager.addSubtask(subtask3);

        System.out.println(manager.showSimpleTask());
        System.out.println(manager.getEpicTasks());
        System.out.println(manager.showSubTasks());



        manager.getSimple(1);
        manager.getEpic(3);
        manager.getSubtask(4);
        manager.getSimple(1);
        manager.getEpic(3);
        manager.getSubtask(4);
        manager.getSimple(1);
        manager.getEpic(3);
        manager.getSubtask(4);
        manager.getSimple(1);
        manager.getEpic(3);
        manager.getSubtask(4);
        manager.getSimple(1);
        manager.getEpic(3);
        manager.getSubtask(4);
        manager.getSimple(1);
        manager.getEpic(3);
        manager.getSubtask(4);

        System.out.println(manager.getHistory());
    }
}
