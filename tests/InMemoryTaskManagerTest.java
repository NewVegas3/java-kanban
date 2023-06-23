import manager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

 class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
  {
   super.taskManager = new InMemoryTaskManager();
  }

}