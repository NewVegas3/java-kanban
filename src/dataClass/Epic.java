package dataClass;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> getSubtasksOfEpic() {
        return subtasksOfEpic;
    }

    protected ArrayList<Integer> subtasksOfEpic = new ArrayList<>();

    public Epic(int id, String title, String description, String status) {
        super(id, title, description, status);
    }


    @Override
    public String toString() {
        return "dataClass.Epic{" +
                "subtasksOfEpic=" + subtasksOfEpic +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
