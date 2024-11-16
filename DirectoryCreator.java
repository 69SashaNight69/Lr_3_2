import java.io.File;
import java.io.IOException;

public class DirectoryCreator {
    public static void main(String[] args) {
        // Назва директорії
        String directoryName = "words";

        // Масив слів для назв файлів
        String[] words = {
                "сонце", "соняшник", "сонячний", "сон", "сонливість",
                "місяць", "місячний", "місячник", "зоря", "зірковий"
        };

        // Створення директорії
        File directory = new File(directoryName);
        if (!directory.exists()) {
            if (directory.mkdir()) {
                System.out.println("Директорію створено: " + directoryName);
            } else {
                System.out.println("Не вдалося створити директорію.");
                return;
            }
        } else {
            System.out.println("Директорія вже існує.");
        }

        // Створення файлів у директорії
        for (String word : words) {
            File file = new File(directory, word + ".txt");
            try {
                if (file.createNewFile()) {
                    System.out.println("Файл створено: " + file.getName());
                } else {
                    System.out.println("Файл вже існує: " + file.getName());
                }
            } catch (IOException e) {
                System.out.println("Помилка під час створення файлу: " + file.getName());
                e.printStackTrace();
            }
        }

        System.out.println("Процес завершено.");
    }
}
