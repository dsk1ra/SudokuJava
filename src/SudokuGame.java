import java.util.Scanner;

public class SudokuGame {
    private SudokuBoard board;
    private Scanner scanner;
    private boolean playWithTimer;
    private TimerThread timerThread;
    private boolean[][] generatedCells;

    public SudokuGame(int size) {
        this.board = new SudokuBoard(size);
        this.scanner = new Scanner(System.in);
        this.playWithTimer = false; // Default to not playing with a timer
    }

    public void startGame() {
        System.out.println("Welcome to Sudoku!");

        // Prompt the user to choose whether to play with a timer
        chooseTimerOption();

        // Start the timer thread if the player chose to play with it
        if (playWithTimer) {
            timerThread = new TimerThread();
            timerThread.start();
        }

        SudokuGenerator generator = new SudokuGenerator(board.getSize(), chooseDifficulty());
        int[][] sudokuGrid = generator.getSudokuGrid();
        populateBoard(sudokuGrid);

        // Initialize generatedCells array
        generatedCells = new boolean[board.getSize()][board.getSize()];
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                generatedCells[i][j] = sudokuGrid[i][j] != 0;
            }
        }

        // Game loop
        while (!isGameFinished()) {
            // Print elapsed time above the board if playing with timer
            if (playWithTimer) {
                System.out.printf("\rTime elapsed: %s", timerThread.getElapsedTime());
            }

            System.out.println("\nCurrent board:");
            board.printBoard(generatedCells); // Pass the generatedCells array as an argument

            System.out.println("Enter your move (row column value), 'solve' to solve the puzzle, or 'q' to quit:");

            // Read user input asynchronously
            String input = null;
            if (scanner.hasNextLine()) {
                input = scanner.nextLine();
            }

            if (input != null) {
                if (input.equalsIgnoreCase("q")) {
                    System.out.println("Quitting the game.");
                    if (playWithTimer && timerThread != null) {
                        timerThread.stopTimer();
                    }
                    break;
                } else if (input.equalsIgnoreCase("solve")) {
                    solveBoard();
                    if (playWithTimer && timerThread != null) {
                        timerThread.stopTimer();
                    }
                    break;
                }

                Move move = parseMove(input);
                if (move != null) {
                    int row = move.getRow();
                    int col = move.getCol();
                    if (!generatedCells[row][col]) { // Check if the cell is empty
                        if (board.isValidMove(row, col, move.getValue())) {
                            board.setCellValue(row, col, move.getValue());
                        } else {
                            System.out.println("Invalid move! Please try again.");
                        }
                    } else {
                        System.out.println("Cannot modify generated cells!");
                    }
                } else {
                    System.out.println("Invalid input! Please enter in the format 'row column value'.");
                }
            }
        }

        // Display congratulations message
        System.out.println("Congratulations! You've completed the Sudoku puzzle.");

        // Print elapsed time after the game is finished
        if (playWithTimer) {
            System.out.printf("\nElapsed time: %s", timerThread.getElapsedTime());
        }
    }


    private void chooseTimerOption() {
        System.out.println("Do you want to play with a timer? (yes/no)");
        String choice = scanner.nextLine();
        playWithTimer = choice.equalsIgnoreCase("yes");
    }

    private int chooseDifficulty() {
        System.out.println("Choose difficulty level:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        System.out.print("Enter the number corresponding to your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        return choice;
    }

    private Move parseMove(String input) {
        String[] parts = input.split(" ");
        if (parts.length == 3) {
            try {
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                int value = Integer.parseInt(parts[2]);
                return new Move(row, col, value);
            } catch (NumberFormatException e) {
                return null; // Invalid input format
            }
        }
        return null; // Invalid input format
    }

    private void populateBoard(int[][] sudokuGrid) {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = sudokuGrid[i][j];
                if (value != 0) {
                    board.setCellValue(i, j, value);
                }
            }
        }
    }

    private void solveBoard() {
        System.out.println("\nSolving the Sudoku puzzle...\n");
        SudokuSolver solver = new SudokuSolver(board.getBoard());
        int[][] solvedGrid = solver.solve();
        if (solvedGrid != null) {
            board.setBoard(solvedGrid); // Update the board with the solved grid
            System.out.println("Solved board:");
            board.printBoard(generatedCells);
        }
    }

    private boolean isGameFinished() {
        // Check if all cells are filled
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getCellValue(i, j) == 0) {
                    return false;
                }
            }
        }

        // Check if the board is valid (no conflicts)
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                int value = board.getCellValue(i, j);
                board.setCellValue(i, j, 0); // Temporarily remove the value
                if (!board.isValidMove(i, j, value)) {
                    return false;
                }
                board.setCellValue(i, j, value); // Restore the value
            }
        }

        return true;
    }

    public class TimerThread extends Thread {
        private volatile boolean running = true;
        private long startTime;

        @Override
        public void run() {
            startTime = System.currentTimeMillis(); // Start the timer
            while (running) {
                // Sleep for 1 second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopTimer() {
            running = false;
        }

        public String getElapsedTime() {
            if (running) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                int seconds = (int) (elapsedTime / 1000) % 60;
                int minutes = (int) (elapsedTime / (1000 * 60)) % 60;
                return String.format("%d minutes and %d seconds", minutes, seconds);
            } else {
                return "0 minutes and 0 seconds";
            }
        }
    }


    public static void main(String[] args) {
        SudokuGame game = new SudokuGame(9); // 9 for standard Sudoku
        game.startGame();
    }
}
