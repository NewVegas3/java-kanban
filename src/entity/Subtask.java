package entity;

import java.time.Instant;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, String title, String description, StatusTask status, int epicId,long duration, Instant startTime) {
        super(id, title, description, status,duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "\n dataClass.Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", duration='" + duration + '\'' +
                ", startTime='" + startTime.toString() +
                '}';
    }

}
