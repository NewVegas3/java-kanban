package entity;

import java.time.Instant;
import java.util.ArrayList;

public class Epic extends Task {

    protected Instant endTime;

    protected ArrayList<Integer> subtasksOfEpic = new ArrayList<>();

    public ArrayList<Integer> getSubtasksOfEpic() {
        return subtasksOfEpic;
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Epic(int id, String title, String description) {
        super(id, title, description ,StatusTask.NEW,0, Instant.ofEpochSecond(0));
    }

    @Override
    public String toString() {
        return "\n dataClass.Epic{" +
                "subtasksOfEpic=" + subtasksOfEpic +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", duration='" + duration + '\'' +
                ", startTime='" + startTime.toString() +
                '}';
    }
}
