public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager.addTask(new Task("Сдать домашку", "Ну надо", StatusTask.NEW));
        TaskManager.addSubtask(new Subtask("Сделать другое", "Ну не очень надо", StatusTask.NEW), 2);
        TaskManager.addEpic(new Epic("Идеальный эпик", "Большой эпик", StatusTask.NEW));
        TaskManager.addSubtask(new Subtask("Сделать другое", "Ну не очень надо", StatusTask.NEW), 2);
        TaskManager.addSubtask(new Subtask("Сделать другое2", "Ну не очень надо", StatusTask.NEW), 2);
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
