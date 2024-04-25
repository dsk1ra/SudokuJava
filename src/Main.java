public class Main {
    public static void main(String[] args) {
        int difficulty = chooseDifficulty();
        boolean playWithTimer = chooseTimerOption();
        GameConfig config = new GameConfig(9, difficulty, playWithTimer); // 9 for standard Sudoku
        SudokuGame game = new SudokuGame(config); // 9 for standard Sudoku
        game.startGame(config);
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