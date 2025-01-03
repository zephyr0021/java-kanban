import statuses.*;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import tasks.*;
import util.Managers;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager inMemoryTaskManager = Managers.getDefault();
        TaskManager inMemoryTaskManager2 = Managers.getDefault();
        printAllTasks(inMemoryTaskManager);
        printWorkingExample(inMemoryTaskManager2);
    }

    private static void printWorkingExample(TaskManager manager) {
        manager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут"));
        manager.addEpic(new Epic("Написать курсовую работу", "Написать курсовую по теме Маркетинг"));
        manager.addSubtask(new Subtask("Собрать теоретическую часть", "Искать в интернете",2));
        manager.addSubtask(new Subtask("Сформировать содержание", "Автоматически",2));
        manager.addSubtask(new Subtask("Выполнить практическую часть", "Провести игру", 2));
        manager.addSubtask(new Subtask("Оформить реферат", "Для выступления", 2));
        manager.addEpic(new Epic("Покрасить волосы", "На праздник"));
        manager.addSubtask(new Subtask("Купить краску", "Для выступления", 7));
        System.out.println("Задачи: ");
        for (Task task: manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Подазадачи эпика - Написать курсовую работу");
        for (Subtask subtask : manager.getEpicSubtasks(2)) {
            System.out.println(subtask);
        }
        System.out.println("Подзадачи эпика - Покрасить волосы");
        for (Subtask subtask : manager.getEpicSubtasks(7)) {
            System.out.println(subtask);
        }

        manager.updateSubtask(new Subtask("Собрать теоретическую часть", "Искать в интернете", StatusTask.DONE,2, 3));
        manager.updateSubtask(new Subtask("Купить краску", "Для выступления", StatusTask.IN_PROGRESS, 7, 8));
        manager.deleteSubtask(4);
        System.out.println();
        System.out.println("После выполнения работ!");
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Подазадачи эпика - Написать курсовую работу");
        for (Subtask subtask : manager.getEpicSubtasks(2)) {
            System.out.println(subtask);
        }
        System.out.println("Подзадачи эпика - Покрасить волосы");
        for (Subtask subtask : manager.getEpicSubtasks(7)) {
            System.out.println(subtask);
        }

        System.out.println("Обновляем эпик");
        manager.updateEpic(new Epic("Покрасить волосы", "На праздник (обновлен)", 7));
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Подзадачи эпика - Покрасить волосы");
        for (Subtask subtask : manager.getEpicSubtasks(7)) {
            System.out.println(subtask);
        }
    }

    private static void printAllTasks(TaskManager manager) {
        manager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут"));
        manager.addEpic(new Epic("Написать курсовую работу", "Написать курсовую по теме Маркетинг"));
        manager.addSubtask(new Subtask("Собрать теоретическую часть", "Искать в интернете",2));
        manager.addSubtask(new Subtask("Сформировать содержание", "Автоматически",2));
        manager.addSubtask(new Subtask("Выполнить практическую часть", "Провести игру", 2));
        manager.addSubtask(new Subtask("Оформить реферат", "Для выступления", 2));
        manager.addEpic(new Epic("Покрасить волосы", "На праздник"));
        manager.addSubtask(new Subtask("Купить краску", "Для выступления", 7));
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
