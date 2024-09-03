import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int rightPathCount = 0;
        System.out.println("Введите путь к файлу: ");
        while (true) {
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (isDirectory) {
                System.out.println("Введен путь к папке! Попробуйте снова...");
                continue;
            } else if (fileExists) {
                System.out.println("Путь указан верно!");
                rightPathCount++;
                System.out.println("Это файл номер: " + rightPathCount);
            }
            else System.out.println("Неверный путь к файлу! Попробуйте снова...");
        }
    }
}