import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Task task = new Task("Сдать домашку", "Ну надо");
        Epic epic = new Epic("Идеальный эпик", "Большой эпик");
        Subtask subtask = new Subtask("Сделать другое", "Ну не очень надо", "Идеальный Эпик");
        TaskManager.addTask(task);
        TaskManager.addTask(epic);
        TaskManager.addTask(subtask);
        for (Integer key : TaskManager.tasks.keySet()) {
            System.out.println(TaskManager.tasks.get(key));
        }
    }
}
