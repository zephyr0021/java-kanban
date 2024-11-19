import java.util.ArrayList;
import java.util.StringJoiner;

public class Epic extends Task {
    private final ArrayList<Integer> subtasks;

    public Epic(String name, String description, StatusTask status) {
        super(name, description, status);
        subtasks = new ArrayList<>();
    }

    private boolean checkNotContainsSubtask(Subtask subtask) {
        if (subtask == null) {
            return false;
        }
        return !subtasks.contains(subtask.getId());
    }

    public void addSubtask(Subtask subtask) {
        if (checkNotContainsSubtask(subtask)) {
            subtasks.add(subtask.getId());
            subtask.setEpicId(this.id);
        } else {
            System.out.println("Данная подзадача уже существует в списке");
        }
    }

    public void removeSubtask(Integer subtaskId) {
        subtasks.remove(subtaskId);
    }

    public ArrayList<Integer> getSubtasksIds() {
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
