import java.util.ArrayList;
import java.util.StringJoiner;

public class Epic extends Task {
    private ArrayList<Integer> subtasks;

    public Epic(String name, String description, int id) {
        super(name, description, id);
        this.subtasks = new ArrayList<>();
    }

    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new ArrayList<>();
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

    public void clearSubtaskList() {
        subtasks.clear();
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
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

    private boolean checkNotContainsSubtask(Subtask subtask) {
        if (subtask == null) {
            return false;
        }
        return !subtasks.contains(subtask.getId());
    }
}
