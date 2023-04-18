import dataClass.Epic;
import dataClass.SimpleTask;
import dataClass.Subtask;
import manager.Manager;

public class Main {


    public static void main(String[] args) {

        SimpleTask simpleTask = new SimpleTask(0, "Переезд", "111","NEW");
        SimpleTask simpleTask1 = new SimpleTask(0, "Переезд1", "111","NEW");

         Epic epic = new Epic(0, "dataClass.Epic", "222", "NEW");
         Subtask subtask = new Subtask(0, "Subtask1", "333", "NEW", 3);
        Subtask subtask1 = new Subtask(0, "Subtask2", "333", "NEW", 3);

        Epic epic1 = new Epic(0, "Epic1", "222", "NEW");
        Subtask subtask3 = new Subtask(0, "Subtask3", "333", "NEW", 6);

        Manager manager = new Manager();


        manager.addSimpleTask(simpleTask);
        manager.addSimpleTask(simpleTask1);

        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.addSubtask(subtask1);

        manager.addEpic(epic1);
        manager.addSubtask(subtask3);

        System.out.println(manager.showSimpleTask());
        System.out.println(manager.showEpicTasks());
        System.out.println(manager.showSubTasks());

        manager.deletionSubtask();
        manager.deletionEpic();
        manager.deletionSubtask();
    }
}
