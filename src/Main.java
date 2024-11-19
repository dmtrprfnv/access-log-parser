import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws GetFileStatisticsExeption{

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

                try {
                    int totalRows = 0, longestLineLength = 0, shortestLineLength = Integer.MAX_VALUE;
                    FileReader fileReader = new FileReader(path);
                    BufferedReader reader =
                            new BufferedReader(fileReader);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        int length = line.length();
                        if (length > 1024) throw new GetFileStatisticsExeption("Row is over 1024 symbol");
                        if (length > longestLineLength) {
                            longestLineLength = length;
                        }
                        if (length < shortestLineLength) {
                            shortestLineLength = length;
                        }
                        totalRows++;
                    }
                    System.out.println("FILE-STATISTICS---------------------------------");
                    System.out.println("longestLineLength = " + longestLineLength + ", " +
                                    "\nshortestLineLength = " + shortestLineLength + ", " +
                                    "\ntotalRows = " + totalRows);

                } catch (GetFileStatisticsExeption e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
            else System.out.println("Неверный путь к файлу! Попробуйте снова...");
        }
    }
}

class GetFileStatisticsExeption extends Exception {
    public GetFileStatisticsExeption() {
        super();
    }

    public GetFileStatisticsExeption(String message) {
        super(message);
    }
}