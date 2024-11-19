public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Task("Сдать домашку", "Ну надо", StatusTask.NEW));
        taskManager.addSubtask(new Subtask("Сделать другое", "Ну не очень надо", StatusTask.NEW,2));
        taskManager.addEpic(new Epic("Идеальный эпик", "Большой эпик", StatusTask.NEW));
        taskManager.addEpic(new Epic("Идеальный эпик2", "Большой эпик", StatusTask.NEW));
        taskManager.addSubtask(new Subtask("Сделать другое", "Ну не очень надо", StatusTask.NEW,2));
        taskManager.addSubtask(new Subtask("Сделать другое2", "Ну не очень надо", StatusTask.NEW,2));
        taskManager.addSubtask(new Subtask("Сделать другое", "Ну не очень надо", StatusTask.NEW, 3));
        taskManager.addSubtask(new Subtask("Сделать другое2", "Ну не очень надо", StatusTask.NEW, 3));
        for (Integer key : taskManager.getTasks().keySet()) {
            System.out.println(taskManager.getTasks().get(key));
        }
        for (Integer key : taskManager.getEpics().keySet()) {
            System.out.println(taskManager.getEpics().get(key));
        }

        for (Integer key : taskManager.getEpicSubtasks(2)) {
            System.out.println(key);
        }

        for (Integer key : taskManager.getSubtasks().keySet()) {
            System.out.println(taskManager.getSubtasks().get(key));
        }

        taskManager.deleteEpic(2);
//        taskManager.deleteTask(1);
//        taskManager.deleteSubtask(7);
//        taskManager.clearSubtasks();
        for (Integer key : taskManager.getTasks().keySet()) {
            System.out.println(taskManager.getTasks().get(key));
        }

        for (Integer key : taskManager.getEpics().keySet()) {
            System.out.println(taskManager.getEpics().get(key));
        }

        for (Integer key : taskManager.getSubtasks().keySet()) {
            System.out.println(taskManager.getSubtasks().get(key));
        }

        taskManager.updateTask(new Task("Сдать домашку", "Ну надо", StatusTask.IN_PROGRESS),1);
        System.out.println(taskManager.getTask(1));
        taskManager.updateSubtask(new Subtask("Сделать другое", "Ну не очень надо", StatusTask.DONE, 3), 6);
        System.out.println(taskManager.getEpic(3));
        taskManager.updateSubtask(new Subtask("Сделать другое", "Ну не очень надо", StatusTask.DONE, 3), 7);
        System.out.println(taskManager.getEpic(3));

    }
}
