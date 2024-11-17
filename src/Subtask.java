public class Subtask extends Task {
    private String epicName;
    public Subtask(String name, String description, String epicName) {
        super(name, description);
        this.epicName = epicName;
    }

    public String getEpicName() {
        return epicName;
    }

    public void setEpicName(String epicName) {
        this.epicName = epicName;
    }
}
