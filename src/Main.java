import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws GetFileStatisticsExeption{

        int rightPathCount = 0;
        System.out.println("Please enter the path to the file: ");
        while (true) {
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (isDirectory) {
                System.out.println("You entered the path to a folder! Please try again...");
                continue;
            } else if (fileExists) {
                System.out.println("Path is correct!");
                rightPathCount++;
                System.out.println("This is file N: " + rightPathCount);

                try {
                    int totalRows = 0, longestLineLength = 0, shortestLineLength = Integer.MAX_VALUE;
                    int googleBotCount = 0, yandexBotCount = 0;
                    Statistics statistics = new Statistics();

                    System.out.println("Reading file. Please wait...");

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

                        LogEntry row = new LogEntry(line);
                        statistics.addEntry(row);


                        Pattern pattern = Pattern.compile("\\(compatible;.*?\\)");
                        Matcher matcher = pattern.matcher(row.userAgent.getUserAgent());

                        if (matcher.find()) {
                            String[] parts = matcher.group(0).split(";");

                            if (parts.length >= 2) {
                                String fragment = parts[1].trim().split("/")[0];
                                if (fragment.equals("YandexBot")) yandexBotCount++;
                                if (fragment.equals("Googlebot")) googleBotCount++;
                            }
                        }
                    }
                    System.out.println("totalRows = " + totalRows);
                    System.out.println("Request rate with YandexBot: " + yandexBotCount + "/" + totalRows + "\n"
                            + "Request rate with GoogleBot: " + googleBotCount + "/" + totalRows);
                    System.out.printf("Statistics: %.3f Mb/hour\n", statistics.getTrafficRate() / 8388608);

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