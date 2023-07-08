package manager;

import java.io.File;

public class Managers {
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTasksManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager(new File("historyFile.csv"));
    }

   public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
