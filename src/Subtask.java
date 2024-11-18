import java.util.StringJoiner;

public class Subtask extends Task {
    private int epicId;
    public Subtask(String name, String description) {
        super(name, description);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Subtask.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("epicId=" + epicId)
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("status=" + status)
                .toString();
    }
}
