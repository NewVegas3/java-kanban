package entity;

import manager.StatusTask;

import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> subtasksOfEpic = new ArrayList<>();

    public ArrayList<Integer> getSubtasksOfEpic() {
        return subtasksOfEpic;
    }

    public Epic(int id, String title, String description, StatusTask status) {
        super(id, title, description, status);
    }

    @Override
    public String toString() {
        return "\n dataClass.Epic{" +
                "subtasksOfEpic=" + subtasksOfEpic +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}