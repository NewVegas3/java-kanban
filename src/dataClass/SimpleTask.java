package dataClass;

public class SimpleTask extends Task {

    public SimpleTask(int id, String title, String description, String status) {
        super(id, title, description, status);
    }


    @Override
    public String toString() {
        return "dataClass.SimpleTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
