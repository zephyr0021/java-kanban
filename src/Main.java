import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут"));
        taskManager.addEpic(new Epic("Написать курсовую работу", "Написать курсовую по теме Маркетинг"));
        taskManager.addSubtask(new Subtask("Собрать теоретическую часть", "Искать в интернете",2));
        taskManager.addSubtask(new Subtask("Сформировать содержание", "Автоматически",2));
        taskManager.addSubtask(new Subtask("Выполнить практическую часть", "Провести игру", 2));
        taskManager.addSubtask(new Subtask("Оформить реферат", "Для выступления", 2));
        taskManager.addEpic(new Epic("Покрасить волосы", "На праздник"));
        taskManager.addSubtask(new Subtask("Купить краску", "Для выступления", 7));

        System.out.println("Задачи: ");
        for (Task task: taskManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Подазадачи эпика - Написать курсовую работу");
        for (Integer key : taskManager.getEpicSubtasks(2)) {
            System.out.println(taskManager.getSubtask(key));
        }
        System.out.println("Подзадачи эпика - Покрасить волосы");
        for (Integer key : taskManager.getEpicSubtasks(7)) {
            System.out.println(taskManager.getSubtask(key));
        }

        taskManager.updateSubtask(new Subtask("Собрать теоретическую часть", "Искать в интернете", StatusTask.DONE,2, 3));
        taskManager.updateSubtask(new Subtask("Купить краску", "Для выступления", StatusTask.IN_PROGRESS, 7, 8));
        taskManager.deleteSubtask(4);
//        taskManager.deleteEpic(7);
//        taskManager.addSubtask(new Subtask("Сходить в парикмахерскую","Парикмахерская перед домом", StatusTask.NEW, 7));
        System.out.println();
        System.out.println("После выполнения работ!");
        System.out.println("Эпики:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Подазадачи эпика - Написать курсовую работу");
        for (Integer key : taskManager.getEpicSubtasks(2)) {
            System.out.println(taskManager.getSubtask(key));
        }
        System.out.println("Подзадачи эпика - Покрасить волосы");
        for (Integer key : taskManager.getEpicSubtasks(7)) {
            System.out.println(taskManager.getSubtask(key));
        }

        System.out.println("Обновляем эпик");
        taskManager.updateEpic(new Epic("Покрасить волосы", "На праздник (обновлен)", 7));
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Подзадачи эпика - Покрасить волосы");
        for (Integer key : taskManager.getEpicSubtasks(7)) {
            System.out.println(taskManager.getSubtask(key));
        }

    }
}
