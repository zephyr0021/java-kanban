import java.util.HashMap;
import java.util.StringJoiner;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;

    public Epic(String name, String description, StatusTask status) {
        super(name, description, status);
        subtasks = new HashMap<>();
    }

    private boolean checkContainsSubtask(Subtask subtask) {
        if (subtask == null) {
            return false;
        }
        return !subtasks.containsKey(subtask.getId());
    }

    public void addSubtask(Subtask subtask) {
        if (checkContainsSubtask(subtask)) {
            subtasks.put(subtask.getId(), subtask);
            subtask.setEpicId(this.id);
        } else {
            System.out.println("Данная подзадача уже существует в списке");
        }
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Epic.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("subtasks size=" + subtasks.size())
                .add("status=" + status)
                .toString();
    }
}
