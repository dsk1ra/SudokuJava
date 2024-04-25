import java.util.Scanner;

/**
 * Utility class for Sudoku game operations.
 */
public class GameUtils {
    static final Scanner scanner = new Scanner(System.in);

    /**
     * Gets user input for integer values.
     *
     * @param message the message to display to the user
     * @return the user's input as an integer
     */
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

    /**
     * Closes the scanner to release resources.
     */
    public static void closeScanner() {
        scanner.close();
    }
}
