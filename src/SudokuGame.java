import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SudokuGame {
    private final SudokuBoard board;
    private final Scanner scanner;
    private boolean playWithTimer;
    private Timer timer;
    private boolean[][] generatedCells;
    private final List<Move> moves;
    private int[][] startBoard;

    public SudokuGame(GameConfig config) {
        this.board = new SudokuBoard(config.getSize());
        this.scanner = new Scanner(System.in);
        this.playWithTimer = config.isPlayWithTimer();
        this.timer = new Timer();
        this.moves = new ArrayList<>();
    }

    public void startGame() {
        System.out.println("Welcome to Sudoku!");

        // Prompt the user to choose whether to play with a timer


        // Start the timer thread if the player chose to play with it
//        if (playWithTimer) {
//            timer.start();
//        }

        GameConfig config = new GameConfig(9, chooseDifficulty(), chooseTimerOption());

        SudokuGenerator generator = new SudokuGenerator(config);
        int[][] sudokuGrid = generator.getSudokuGrid();
        populateBoard(sudokuGrid);
        startBoard = board.getBoard();
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
                System.out.printf("\rTime elapsed: %s", timer.getElapsedTime());
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
                    if (playWithTimer && timer != null) {
                        timer.stop();
                    }
                    break;
                } else if (input.equalsIgnoreCase("solve")) {
                    solveBoard();
                    if (playWithTimer && timer != null) {
                        timer.stop();
                    }
                    break;
                }

                Move move = parseMove(input);
                if (move != null) {
                    moves.add(move); // Add the move to the replay only if it's not null
                    int row = move.getRow();
                    int col = move.getCol();
                    if (!generatedCells[row][col]) { // Check if the cell is empty
                        board.setCellValue(row, col, move.getValue());
                    } else {
                        System.out.println("Cannot modify generated cells!");
                    }
                } else {
                    System.out.println("Invalid input! Please enter in the format 'row column value'.");
                }
            }
        }
// Create a Replay object after the game is finished
        String elapsedTime;
        if (playWithTimer) {
            assert timer != null;
            elapsedTime = timer.getElapsedTime();
        } else {
            elapsedTime = "0 minutes and 0 seconds";
        }
        Replay replay = new Replay(moves, elapsedTime);

        // Display congratulations message
        System.out.println("Congratulations! You've completed the Sudoku puzzle.");

        // Print elapsed time after the game is finished
        if (playWithTimer) {
            System.out.printf("\nElapsed time: %s", elapsedTime);
        }

        // Save the replay or display replay options
        saveOrDisplayReplay(replay);
    }

    private void saveOrDisplayReplay(Replay replay) {
        System.out.println("1. Save replay");
        System.out.println("2. Load and replay saved game");
        System.out.println("3. Exit");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (choice) {
            case 1:
                // Prompt the user to enter the filename of the saved replay
                System.out.print("Enter the filename of the saved replay: ");
                String filename = scanner.nextLine();

                // Save the initial board state and the replay data
                try (PrintWriter writer = new PrintWriter(filename + ".txt")) {
                    // Write the initial board state to the first lines of the file
                    for (int i = 0; i < startBoard.length; i++) {
                        for (int j = 0; j < startBoard[i].length; j++) {
                            writer.printf("%d ", startBoard[i][j]);
                        }
                        writer.println();
                    }

                    // Write the elapsed time and moves data to the remaining lines of the file
                    writer.println(replay.getElapsedTime());
                    for (Move move : replay.getMoves()) {
                        writer.printf("%d %d %d\n",move.getRow(), move.getCol(), move.getValue());
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error: File not found.");
                }

                System.out.println("Replay saved.");
                break;
            case 2:
                // Implement loading the replay from a file
                Replay loadedReplay = loadReplay();
                if (loadedReplay != null) {
                    loadAndReplayGame(loadedReplay, board.getBoard());
                    System.out.println("Elapsed time: " + loadedReplay.getElapsedTime());
                }
                break;
            case 3:
                System.out.println("Exiting the game.");
                break;
            default:
                System.out.println("Invalid choice. Exiting the game.");
                break;
        }
    }

    private Replay loadReplay() {
        // Prompt the user to enter the filename of the saved replay
        System.out.print("Enter the filename of the saved replay: ");
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.nextLine();

        // Try to open the file and read the saved replay data
        File file = new File(filename + ".txt");
        if (!file.exists()) {
            System.out.println("Error: File not found.");
            return null;
        }

        int[][] board = new int[9][9];
        List<Move> moves = new ArrayList<>();
        String elapsedTime = "";

        try (Scanner fileScanner = new Scanner(file)) {
            // Read the sudoku board data
            for (int i = 0; i < 9; i++) {
                String[] rowData = fileScanner.nextLine().split(" ");
                for (int j = 0; j < 9; j++) {
                    board[i][j] = Integer.parseInt(rowData[j]);
                }
            }
            System.out.println("Loaded Sudoku Board:");
            printBoard(board);
            // Read the elapsed time line
            elapsedTime = fileScanner.nextLine();

            // Read the moves data from the remaining lines of the file
            while (fileScanner.hasNextLine()) {
                String[] moveData = fileScanner.nextLine().split(" ");
                int row = Integer.parseInt(moveData[0]);
                int col = Integer.parseInt(moveData[1]);
                int value = Integer.parseInt(moveData[2]);
                moves.add(new Move(row, col, value));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid replay data.");
            return null;
        }

        // Return the Replay object created from the saved replay data
        return new Replay(board, moves, elapsedTime);
    }

    private void printBoard(int[][] board) {
        // Print column numbers
        System.out.print("  ");
        for (int i = 0; i < 9; i++) {
            System.out.print(i + " ");
            if ((i + 1) % 3 == 0 && i < 9 - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();

        // Print horizontal line
        System.out.print(" ");
        for (int i = 0; i < 9 + (9 / 3 - 1); i++) {
            System.out.print("--");
        }
        System.out.println();

        // Print board contents
        for (int i = 0; i < 9; i++) {
            // Print row number
            System.out.print(i + "|");

            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) { // Check if the cell value is zero
                    System.out.print("\u001B[32m" + board[i][j] + " " + "\u001B[0m"); // Blue color
                } else {
                    System.out.print("\u001B[34m" + board[i][j] + " " + "\u001B[0m"); // Green color
                }

                if ((j + 1) % 3 == 0 && j < 9 - 1) {
                    System.out.print("| ");
                }
            }
            System.out.println();

            // Print horizontal line after every 3 rows
            if ((i + 1) % 3 == 0 && i < 9 - 1) {
                System.out.print(" ");
                for (int j = 0; j < 9 + (9 / 3 - 1); j++) {
                    System.out.print("--");
                }
                System.out.println();
            }
        }
    }

    private void loadAndReplayGame(Replay replay, int[][] initialBoard) {
        // Reset the board to its initial state
        board.setBoard(initialBoard);

        // Replay the moves
        for (Move move : replay.getMoves()) {
            int row = move.getRow();
            int col = move.getCol();
            System.out.println(board.getCellValue(row, col));
            if (board.isValidMove(row, col, move.getValue())) {
                board.setCellValue(row, col, move.getValue());
                System.out.println("Replaying move: " + move);
                System.out.println("Current board:");
                board.printBoard(generatedCells);
            } else {
                System.out.println("Invalid move! Skipping...");
            }
        }
    }

    private boolean chooseTimerOption() {
        System.out.println("Do you want to play with a timer? (0 - no / 1 - yes)");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        return choice == 1;
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
        Pattern pattern = Pattern.compile("(\\d+) (\\d+) (\\d+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            int row = Integer.parseInt(matcher.group(1));
            int col = Integer.parseInt(matcher.group(2));
            int value = Integer.parseInt(matcher.group(3));

            if (board.isValidMove(row, col, value)) {
                return new Move(row, col, value);
            } else {
                System.out.println("Invalid move! Please try again.");
            }
        } else {
            System.out.println("Invalid input! Please enter in the format 'row column value'.");
        }
        return null;
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
            board.setBoard(solvedGrid); // Update the boardwith the solved grid
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
}

