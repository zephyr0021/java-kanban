import statuses.*;
import managers.TaskManager;
import tasks.*;
import util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager inMemoryTaskManager = Managers.getDefault();
        TaskManager fileBackedTaskManager = Managers.getDefaultFileBackend();
//        printHistoryDeleteTaskWorkExample(inMemoryTaskManager);
//        printWorkingExampleWithFile(fileBackedTaskManager);
//        printWorkingExampleFromFile(file);
        printWorkinExamplePrioritizedTasks(inMemoryTaskManager);
    }

//    private static void printWorkingExample(TaskManager manager) {
//        manager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут"));
//        manager.addEpic(new Epic("Написать курсовую работу", "Написать курсовую по теме Маркетинг"));
//        manager.addSubtask(new Subtask("Собрать теоретическую часть", "Искать в интернете", 2));
//        manager.addSubtask(new Subtask("Сформировать содержание", "Автоматически", 2));
//        manager.addSubtask(new Subtask("Выполнить практическую часть", "Провести игру", 2));
//        manager.addSubtask(new Subtask("Оформить реферат", "Для выступления", 2));
//        manager.addEpic(new Epic("Покрасить волосы", "На праздник"));
//        manager.addSubtask(new Subtask("Купить краску", "Для выступления", 7));
//        System.out.println("Задачи: ");
//        for (Task task : manager.getTasks()) {
//            System.out.println(task);
//        }
//        System.out.println("Эпики:");
//        for (Epic epic : manager.getEpics()) {
//            System.out.println(epic);
//        }
//        System.out.println("Подазадачи эпика - Написать курсовую работу");
//        for (Subtask subtask : manager.getEpicSubtasks(2)) {
//            System.out.println(subtask);
//        }
//        System.out.println("Подзадачи эпика - Покрасить волосы");
//        for (Subtask subtask : manager.getEpicSubtasks(7)) {
//            System.out.println(subtask);
//        }
//
//        manager.updateSubtask(new Subtask("Собрать теоретическую часть", "Искать в интернете", StatusTask.DONE, 2, 3));
//        manager.updateSubtask(new Subtask("Купить краску", "Для выступления", StatusTask.IN_PROGRESS, 7, 8));
//        manager.deleteSubtask(4);
//        System.out.println();
//        System.out.println("После выполнения работ!");
//        System.out.println("Эпики:");
//        for (Epic epic : manager.getEpics()) {
//            System.out.println(epic);
//        }
//        System.out.println("Подазадачи эпика - Написать курсовую работу");
//        for (Subtask subtask : manager.getEpicSubtasks(2)) {
//            System.out.println(subtask);
//        }
//        System.out.println("Подзадачи эпика - Покрасить волосы");
//        for (Subtask subtask : manager.getEpicSubtasks(7)) {
//            System.out.println(subtask);
//        }
//
//        System.out.println("Обновляем эпик");
//        manager.updateEpic(new Epic("Покрасить волосы", "На праздник (обновлен)", 7));
//        for (Epic epic : manager.getEpics()) {
//            System.out.println(epic);
//        }
//        System.out.println("Подзадачи эпика - Покрасить волосы");
//        for (Subtask subtask : manager.getEpicSubtasks(7)) {
//            System.out.println(subtask);
//        }
//    }
//
//    private static void printAllTasks(TaskManager manager) {
//        manager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут"));
//        manager.addEpic(new Epic("Написать курсовую работу", "Написать курсовую по теме Маркетинг"));
//        manager.addSubtask(new Subtask("Собрать теоретическую часть", "Искать в интернете", 2));
//        manager.addSubtask(new Subtask("Сформировать содержание", "Автоматически", 2));
//        manager.addSubtask(new Subtask("Выполнить практическую часть", "Провести игру", 2));
//        manager.addSubtask(new Subtask("Оформить реферат", "Для выступления", 2));
//        manager.addEpic(new Epic("Покрасить волосы", "На праздник"));
//        manager.addSubtask(new Subtask("Купить краску", "Для выступления", 7));
//        System.out.println("Задачи:");
//        for (Task task : manager.getTasks()) {
//            System.out.println(task);
//        }
//        System.out.println("Эпики:");
//        for (Task epic : manager.getEpics()) {
//            System.out.println(epic);
//
//            for (Task task : manager.getEpicSubtasks(epic.getId())) {
//                System.out.println("--> " + task);
//            }
//        }
//        System.out.println("Подзадачи:");
//        for (Task subtask : manager.getSubtasks()) {
//            System.out.println(subtask);
//        }
//
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//    }
//
//    private static void printHistoryWorkExample(TaskManager manager) {
//        manager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут"));
//        manager.addEpic(new Epic("Написать курсовую работу", "Написать курсовую по теме Маркетинг"));
//        manager.addSubtask(new Subtask("Собрать теоретическую часть", "Искать в интернете", 2));
//        manager.addSubtask(new Subtask("Сформировать содержание", "Автоматически", 2));
//        manager.addSubtask(new Subtask("Выполнить практическую часть", "Провести игру", 2));
//        manager.addSubtask(new Subtask("Оформить реферат", "Для выступления", 2));
//        manager.getTask(1);
//        manager.getTask(1);
//        manager.getTask(1);
//        manager.getEpic(2);
//        manager.getSubtask(3);
//        manager.getEpic(2);
//        manager.getSubtask(3);
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//    }
//
//    private static void printHistoryDeleteTaskWorkExample(TaskManager manager) {
//        manager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут"));
//
//        manager.addTask(new Task("Сделать ДЗ", "Сделать ДЗ за 1 час"));
//        manager.addEpic(new Epic("Написать курсовую работу", "Написать курсовую по теме Маркетинг"));
//        manager.addSubtask(new Subtask("Собрать теоретическую часть", "Искать в интернете", 3));
//        manager.addSubtask(new Subtask("Сформировать содержание", "Автоматически", 3));
//        manager.addSubtask(new Subtask("Выполнить практическую часть", "Провести игру", 3));
//        manager.addEpic(new Epic("Сделать проект по 6 спринту яндекс", "Выполнить все подзадачи"));
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//        manager.getTask(2);
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//        manager.getEpic(3);
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//        manager.getEpic(7);
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//        manager.getTask(1);
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//        manager.getSubtask(4);
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//        manager.getSubtask(6);
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//        manager.getEpic(2);
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//
//        manager.deleteTask(1);
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//        manager.deleteEpic(3);
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//    }

    private static void printWorkingExampleWithFile(TaskManager manager) {
        manager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут", Duration.ofMinutes(100), LocalDateTime.now()));
        manager.addEpic(new Epic("Написать курсовую работу", "Написать курсовую по теме Маркетинг"));
        manager.addSubtask(new Subtask("Собрать теоретическую часть", "Искать в интернете", 2, Duration.ofMinutes(150), LocalDateTime.now()));
        manager.addSubtask(new Subtask("Сформировать содержание", "Автоматически", 2, Duration.ofMinutes(180), LocalDateTime.now()));
        manager.addSubtask(new Subtask("Выполнить практическую часть", "Провести игру", 2, Duration.ofMinutes(120), LocalDateTime.now()));
        manager.addEpic(new Epic("Покрасить волосы", "На праздник"));
        manager.addSubtask(new Subtask("Оформить реферат", "Для выступления", 6, Duration.ofMinutes(60), LocalDateTime.now()));
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
        manager.updateSubtask(new Subtask("Собрать теоретическую часть", "Искать в интернете", StatusTask.DONE, 2, 3, Duration.ofMinutes(150), LocalDateTime.now()));
        System.out.println("После выполнения работ:");
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
        manager.deleteSubtask(3);
        manager.deleteEpic(2);
        manager.updateSubtask(new Subtask("Оформить реферат", "Для выступления", StatusTask.DONE, 6, 7, Duration.ofMinutes(60), LocalDateTime.now()));
        System.out.println("После выполнения работ:");
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
    }

    private static void printWorkinExamplePrioritizedTasks(TaskManager manager) {
        manager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут", Duration.ofMinutes(100), LocalDateTime.of(2025, 6, 2, 10, 0)));
        manager.addTask(new Task("Выгулять собаку", "Погулять с Джеком 20 минут3.4"));
        manager.addTask(new Task("Выгулять собаку2", "Погулять с Джеком 20 минут2", Duration.ofMinutes(100), LocalDateTime.of(2025, 6, 2, 10, 10)));
        manager.addTask(new Task("Выгулять собаку3", "Погулять с Джеком 20 минут3", Duration.ofMinutes(100), LocalDateTime.of(2025, 7, 2, 9, 0)));
        manager.addTask(new Task("Выгулять собаку4", "Погулять с Джеком 20 минут4", Duration.ofMinutes(100), LocalDateTime.of(2025, 6, 2, 10, 0)));
        manager.addEpic(new Epic("Выгулять собаку5", "Погулять с Джеком 20 минут5"));
        manager.addSubtask(new Subtask("Выгулять собаку6", "Погулять с Джеком 20 минут6", 6, Duration.ofMinutes(100), LocalDateTime.of(2025, 6, 2, 10, 0)));
        manager.getPrioritizedTasks().forEach(System.out::println);
        manager.getPrioritizedSubtasks().forEach(System.out::println);
        manager.getPrioritizedEpics().forEach(System.out::println);
    }

//    private static void printWorkingExampleFromFile(File file) {
//        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
//        System.out.println("Задачи:");
//        for (Task task : manager.getTasks()) {
//            System.out.println(task);
//        }
//        System.out.println("Эпики:");
//        for (Epic epic : manager.getEpics()) {
//            System.out.println(epic);
//        }
//        System.out.println("Подзадачи:");
//        for (Subtask subtask : manager.getSubtasks()) {
//            System.out.println(subtask);
//        }
//        manager.addTask(new Task("Тест1", "Погулять с Джеком 20 минут"));
//        manager.addTask(new Task("Тест2", "Погулять с Джеком 20 минут"));
//        manager.addTask(new Task("Тест3", "Погулять с Джеком 20 минут"));
//        manager.addTask(new Task("Тест4", "Погулять с Джеком 20 минут"));
//        manager.addTask(new Task("Тест5", "Погулять с Джеком 20 минут"));
//        manager.addTask(new Task("Тест6", "Погулять с Джеком 20 минут"));
//        manager.addTask(new Task("Тест7", "Погулять с Джеком 20 минут"));
//    }
}
