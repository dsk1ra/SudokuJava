public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Sudoku!");
        System.out.println("1. Play a new game");
        System.out.println("2. Watch a replay");
        System.out.print("Enter the number corresponding to your choice: ");

        int choice = GameUtils.scanner.nextInt();
        GameUtils.scanner.nextLine(); // Consume newline character

        if (choice == 1) {
            int difficulty = chooseDifficulty();
            boolean playWithTimer = chooseTimerOption();
            GameConfig config = new GameConfig(9, difficulty, playWithTimer); // 9 for standard Sudoku
            SudokuGame game = new SudokuGame(config); // 9 for standard Sudoku
            game.startGame(config);
        } else if (choice == 2) {
            SudokuGame game = new SudokuGame(new GameConfig(9, 1, false)); // dummy config
            game.saveOrDisplayReplay(null); // pass null to load a replay
        } else {
            System.out.println("Invalid choice. Exiting.");
        }
    }

    private static int chooseDifficulty() {
        System.out.println("Choose difficulty level:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        System.out.print("Enter the number corresponding to your choice: ");

        int choice = GameUtils.scanner.nextInt();
        GameUtils.scanner.nextLine(); // Consume newline character
        return choice;
    }

    private static boolean chooseTimerOption() {
        System.out.println("Do you want to play with a timer? (0 - no / 1 - yes)");

        int choice = GameUtils.scanner.nextInt();
        GameUtils.scanner.nextLine(); // Consume newline character

        return choice == 1;
    }
}