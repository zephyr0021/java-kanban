public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager.addTask(new Task("Сдать домашку", "Ну надо"));
        TaskManager.addEpic(new Epic("Идеальный эпик", "Большой эпик"));
        TaskManager.addSubtask(new Subtask("Сделать другое", "Ну не очень надо"), 2);
        for (Integer key : TaskManager.getTasks().keySet()) {
            System.out.println(TaskManager.getTasks().get(key));
        }
        for (Integer key : TaskManager.getSubtasks().keySet()) {
            System.out.println(TaskManager.getSubtasks().get(key));
        }
        for (Integer key : TaskManager.getEpics().keySet()) {
            System.out.println(TaskManager.getEpics().get(key));
        }

        for (Integer key : TaskManager.getEpicSubtasks(2).keySet()) {
            System.out.println(TaskManager.getEpicSubtasks(2).get(key));
        }
    }
}
