import java.util.Objects;
import java.util.StringJoiner;

public class Task {
    protected String name;
    protected String description;
    protected StatusTask status;
    protected int id;

    public Task(String name, String description, StatusTask status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public Task(String name, String description, StatusTask status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = StatusTask.NEW;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StatusTask getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getTypeName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("status=" + status)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        if (id == task.id) return true;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }
}
