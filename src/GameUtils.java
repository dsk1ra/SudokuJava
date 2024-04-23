import java.util.Scanner;

public class GameUtils {
    private static final Scanner scanner = new Scanner(System.in);

    // Method to get user input for integer values
    public static int getUserInput(String message) {
        System.out.print(message + ": ");
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter an integer: ");
            }
        }
    }

    // Method to validate user input for cell coordinates
    public static boolean isValidCellInput(int input, int boardSize) {
        return input >= 0 && input < boardSize;
    }

    // Close the scanner to release resources
    public static void closeScanner() {
        scanner.close();
    }
}
