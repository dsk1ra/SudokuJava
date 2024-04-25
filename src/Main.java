public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Sudoku!");
        System.out.println("1. Play a new game");
        System.out.println("2. Watch a replay");

        int choice = getUserChoice();

        if (choice == 1) {
            int difficulty = getDifficultyChoice();
            boolean playWithTimer = getTimerOption();
            GameConfig config = new GameConfig(9, difficulty, playWithTimer); // 9 for standard Sudoku
            SudokuGame game = new SudokuGame(config); // 9 for standard Sudoku
            game.startGame(config);
        } else if (choice == 2) {
            SudokuGame game = new SudokuGame(new GameConfig(9, 1, false)); // dummy config
            game.saveOrDisplayReplay(null); // pass null to load a replay
        }
    }

    private static int getUserChoice() {
        while (true) {
            int choice = GameUtils.getUserInput("Enter the number corresponding to your choice");
            if (choice >= 1 && choice <= 2) {
                return choice;
            } else {
                System.out.println("Invalid choice. Please enter a number between " + 1 + " and " + 2 + ".");
            }
        }
    }

    private static int getDifficultyChoice() {
        while (true) {
            System.out.println("Choose difficulty level");
            System.out.println("1. Easy");
            System.out.println("2. Medium");
            System.out.println("3. Hard");
            System.out.println("4. Custom");
            int difficulty = GameUtils.getUserInput("Enter the number corresponding to your choice");
            if (difficulty >= 1 && difficulty <= 3) {
                return difficulty;
            } else if (difficulty == 4) {
                System.out.println("Custom difficulty chosen. Enter the percentage of the board you want to be filled (0-90):");
                int percentage = GameUtils.getUserInput("Enter the percentage");
                if (percentage >= 10 && percentage <= 90) {
                    return -1 * percentage; // return a negative value to indicate custom difficulty
                } else {
                    System.out.println("Invalid percentage. Please enter a number between 10 and 90.");
                }
            } else {
                System.out.println("Invalid difficulty level. Please enter a number between 1 and 4.");
            }
        }
    }

    private static boolean getTimerOption() {
        while (true) {
            int choice = GameUtils.getUserInput("Do you want to play with a timer? (0 - no / 1 - yes)");
            if (choice == 0 || choice == 1) {
                return choice == 1;
            } else {
                System.out.println("Invalid choice. Please enter 0 or 1.");
            }
        }
    }
}