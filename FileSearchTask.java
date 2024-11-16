import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class FileSearchTask extends RecursiveTask<List<String>> { // Розширення RecursiveTask для Fork/Join
    private final File directory;
    private final String searchWord;

    public FileSearchTask(File directory, String searchWord) {
        this.directory = directory;
        this.searchWord = searchWord.toLowerCase();
    }

    @Override
    protected List<String> compute() {
        List<String> foundFiles = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files == null) {
            return foundFiles;
        }

        List<FileSearchTask> subTasks = new ArrayList<>(); // Список підзадач

        for (File file : files) {
            if (file.isDirectory()) {
                // Якщо це директорія, створюємо підзадачу для обробки піддиректорії
                FileSearchTask task = new FileSearchTask(file, searchWord);
                subTasks.add(task);
                task.fork(); // Запускаємо завдання у фоновому режимі
            } else if (file.getName().toLowerCase().contains(searchWord)) {
                // Якщо файл містить шукане слово, додаємо його до результатів
                foundFiles.add(file.getAbsolutePath());
            }
        }

        // Об'єднуємо результати підзадач
        for (FileSearchTask task : subTasks) {
            foundFiles.addAll(task.join()); // Вичікуємо результати від підзадач
        }

        return foundFiles;
    }

    public static void main(String[] args) {
        // Фіксований шлях до директорії
        String directoryPath = "D:\\KPI\\3курс\\Асинхрон\\Lr_3\\Lr_3_2\\words";

        // Введення слова для пошуку
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("Введіть літеру або слово для пошуку:");
        String searchWord = scanner.nextLine();

        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Некоректний шлях до директорії: " + directoryPath);
            return;
        }

        // Створення пулу потоків ForkJoinPool для паралельної обробки
        ForkJoinPool pool = new ForkJoinPool(); // Пул потоків ForkJoin
        FileSearchTask task = new FileSearchTask(directory, searchWord); // Створення основної підзадачі

        System.out.println("Пошук файлів...");
        List<String> result = pool.invoke(task); // Запуск головного завдання

        // Виведення результатів пошуку
        if (result.isEmpty()) {
            System.out.printf("Файли з '%s' у назві не знайдено.%n", searchWord);
        } else {
            System.out.printf("Кількість файлів, що містять '%s' у назві: %d%n", searchWord, result.size());
            System.out.println("Знайдені файли:");
            result.forEach(System.out::println);
        }

        // Закриття пулу потоків
        pool.shutdown();
    }
}
