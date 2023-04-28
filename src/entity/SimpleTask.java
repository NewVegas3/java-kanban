package entity;

import manager.StatusTask;

public class SimpleTask extends Task {

    public SimpleTask(int id, String title, String description, StatusTask status) {
        super(id, title, description, status);
    }

    @Override
    public String toString() {
        return "\n dataClass.SimpleTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
