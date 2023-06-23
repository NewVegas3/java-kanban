package entity;

import java.time.Instant;

public class SimpleTask extends Task {

    public SimpleTask(int id, String title, String description, StatusTask status,long duration, Instant startTime) {
        super(id, title, description, status,duration, startTime);
    }

    @Override
    public String toString() {
        return "\n dataClass.SimpleTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", duration='" + duration + '\'' +
                ", startTime='" + startTime.toString() +
                '}';
    }
}
