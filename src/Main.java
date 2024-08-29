import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число: ");
        int firstNumber = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число: ");
        int secondNumber = new Scanner(System.in).nextInt();

        System.out.println("====================");
        System.out.printf("Сумма: %d \n", firstNumber+secondNumber);
        System.out.printf("Разность: %d \n", firstNumber-secondNumber);
        System.out.printf("Произведение: %d \n", firstNumber*secondNumber);
        System.out.printf("Частное: %.3f \n", (double)firstNumber/secondNumber);
        System.out.println("====================");
    }
}
