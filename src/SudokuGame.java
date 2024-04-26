import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The SudokuGame class represents the main game logic for a Sudoku game.
 * It includes methods for starting a new game, handling user input, and managing game state.
 */
public class SudokuGame {

    /**
     * The SudokuBoard object representing the current game board.
     */
    private SudokuBoard board;

    /**
     * The Scanner object used for reading user input.
     */
    private final Scanner scanner;

    /**
     * A boolean value indicating whether the game is being played with a timer.
     */
    private final boolean playWithTimer;

    /**
     * The Timer object used for tracking elapsed time when playing with a timer.
     */
    private final Timer timer;

    /**
     * A 2D boolean array indicating which cells on the board were generated (pre-filled) at the start of the game.
     */
    private boolean[][] generatedCells;

    /**
     * A List of Move objects representing the moves made during the game.
     */
    private final List<Move> moves;

    /**
     * A 2D integer array representing the initial state of the board at the start of the game.
     */
    private int[][] startBoard;

    /**
     * A MoveHistory object representing the history of moves made during the game.
     */
    private MoveHistory moveHistory;

    /**
     * Constructs a new SudokuGame object with the given GameConfig object.
     *
     * @param config The GameConfig object containing the game configuration settings.
     */
    public SudokuGame(GameConfig config) {
        this.board = new SudokuBoard(config.getSize());
        this.scanner = new Scanner(System.in);
        this.playWithTimer = config.isPlayWithTimer();
        this.timer = new Timer();
        this.moves = new ArrayList<>();
        this.moveHistory = new MoveHistory();
    }

    /**
     * Clears the board and resets the game state.
     */
    public void clearBoard() {
        board = new SudokuBoard(board.getSize());
        moves.clear();
        generatedCells = new boolean[board.getSize()][board.getSize()];
        moveHistory = new MoveHistory();
    }

    /**
     * Starts a new game with the given configuration.
     *
     * @param config the game configuration
     */
    public void startGame(GameConfig config) {
        clearBoard(); // Clear the board before starting a new game
        System.out.println("Welcome to Sudoku!");

        SudokuGenerator generator = new SudokuGenerator(config);
        int[][] sudokuGrid = generator.getSudokuGrid();
        populateBoard(sudokuGrid);
        startBoard = board.getBoard();

        // Initialize generatedCells array
        getGeneratedCells(sudokuGrid);

        // Initialize elapsed time
        if (playWithTimer) {
            timer.start();
        }

        // Game loop
        while (!isGameFinished()) {
            // Print elapsed time above the board if playing with timer
            if (playWithTimer) {
                System.out.printf("\rTime elapsed: %s", timer.getElapsedTime());
            }

            System.out.println("\nCurrent board:");
            board.printBoard(generatedCells); // Pass the generatedCells array as an argument

            System.out.println("Enter your move (row column value)\n'solve' to solve the puzzle\n'q' to quit\n'u' to undo\n'r' to redo:");
            System.out.println("Remaining moves: " + getRemainingMoves());

            // Read user input asynchronously
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Quitting the game.");
                if (playWithTimer) {
                    timer.stop();
                }
                break;
            } else if (input.equalsIgnoreCase("solve")) {
                solveBoard();
                System.out.printf("Time elapsed: %s \n", timer.getElapsedTime());
                if (playWithTimer) {
                    timer.stop();
                }
                break;
            } else if (input.equalsIgnoreCase("u")) {
                undo();
            } else if (input.equalsIgnoreCase("r")) {
                redo();
            } else {
                Move move = parseMove(input);
                if (move != null) {
                    moveHistory.addMove(move); // Add the move to the replay only if it's not null
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

        // Stop the timer and get the elapsed time
        // Add elapsedTime as a class-level variable
        String elapsedTime;
        if (playWithTimer) {
            timer.stop();
            elapsedTime = timer.getElapsedTime();
        } else {
            elapsedTime = "0 minutes and 0 seconds";
        }

        // After the game is finished, prompt the user for next action
        System.out.println("Game over!");
        System.out.println("1. Play another game");
        System.out.println("2. Save current replay");
        System.out.println("3. Load and replay saved game");
        System.out.println("4. Exit");
        System.out.print("Enter the number corresponding to your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (choice) {
            case 1:
                // Play another game
                System.out.println("Enter a new game configuration:");
                config = getNewGameConfig();
                System.out.println("Starting a new game...");
                startGame(config);
                break;
            case 2:
                // Save current replay
                saveOrDisplayReplay();
                break;
            case 3:
                // Load and replay saved game
                Replay loadedReplay = loadReplay();
                if (loadedReplay != null) {
                    loadAndReplayGame(loadedReplay, board.getBoard());
                }
                break;
            case 4:
                // Exit
                System.out.println("Exiting the game. Goodbye!");
                GameUtils.closeScanner();
                break;
            default:
                System.out.println("Invalid choice. Exiting the game.");
                break;
        }
    }

    /**
     * Returns a new GameConfig object based on user input.
     *
     * @return a new GameConfig object
     */
    private GameConfig getNewGameConfig() {
        System.out.println("Choose difficulty level:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        System.out.print("Enter the number corresponding to your choice: ");
        int difficulty = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        System.out.print("Do you want to play with a timer? (0 - no / 1 - yes): ");
        boolean playWithTimer = scanner.nextInt() == 1;
        scanner.nextLine(); // Consume newline character

        return new GameConfig(9, difficulty, playWithTimer);
    }

    /**
     * Saves or displays a replay of the game.
     *
     * @param replay the replay data
     */
    void saveOrDisplayReplay() {
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
                    for (int[] ints : startBoard) {
                        for (int anInt : ints) {
                            writer.printf("%d ", anInt);
                        }
                        writer.println();
                    }

                    // Write the elapsed time
                    writer.println(timer.getElapsedTime());

                    // Write the moves in the format "row col val"
                    for (Move move : moveHistory.moves) {
                        writer.printf("%d %d %d\n", move.getRow(), move.getCol(), move.getValue());
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

    /**
     * Returns the number of remaining moves in the game.
     *
     * @return the number of remaining moves
     */
    private int getRemainingMoves() {
        int remainingMoves = 0;
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getCellValue(i, j) == 0) {
                    remainingMoves++;
                }
            }
        }
        return remainingMoves;
    }

    /**
     * Loads a replay from a file.
     *
     * @return the loaded replay data, or null if loading fails
     */
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

        int[][] initialBoard = new int[9][9];
        List<Move> moves = new ArrayList<>();
        String elapsedTime;

        try (Scanner fileScanner = new Scanner(file)) {
            // Read the sudoku board data
            for (int i = 0; i < 9; i++) {
                String[] rowData = fileScanner.nextLine().split(" ");
                for (int j = 0; j < 9; j++) {
                    initialBoard[i][j] = Integer.parseInt(rowData[j]);
                }
            }
            System.out.println("Loaded Sudoku Board:");
            printBoard(initialBoard);

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

        // Create a new SudokuBoard with the loaded initial board state
        this.board = new SudokuBoard(9); // Assign the loaded board to the board field

        // Initialize generatedCells array
        getGeneratedCells(initialBoard);

        // Populate the current board with the loaded data
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (generatedCells[i][j]) {
                    board.setCellValue(i, j, initialBoard[i][j]);
                } else {
                    board.setCellValue(i, j, 0);
                }
            }
        }

        // Return the Replay object created from the saved replay data
        return new Replay(initialBoard, moves, elapsedTime);
    }

    /**
     * Initializes the generatedCells array based on the initial board state.
     *
     * @param initialBoard the initial board state
     */
    private void getGeneratedCells(int[][] initialBoard) {
        generatedCells = new boolean[board.getSize()][board.getSize()];
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                generatedCells[i][j] = initialBoard[i][j] != 0;
            }
        }
    }

    /**
     * Prints the board to the console.
     *
     * @param board the board to print
     */
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

    /**
     * Loads and replays a game from a saved replay.
     *
     * @param replay       the replay data
     * @param initialBoard the initial board state
     */
    private void loadAndReplayGame(Replay replay, int[][] initialBoard) {
        // Reset the board to its initial state
        board.setBoard(initialBoard);

        // Replay the moves
        int i = 0;
        for (Move move : replay.getMoves()) {
            i++;
            int row = move.getRow();
            int col = move.getCol();
            System.out.println(board.getCellValue(row, col));
            if (board.isValidMove(row, col, move.getValue())) {
                board.setCellValue(row, col, move.getValue());
                System.out.println("Replaying move: " + i);
                System.out.println("Current board:");
                board.printBoard(generatedCells);
            } else {
                System.out.println("Invalid move! Skipping...");
            }
        }
    }

    /**
     * Parses a user input string into a Move object.
     *
     * @param input the user input string
     * @return the parsed Move object, or null if parsing fails
     */
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

    /**
     * Populates the board with the given Sudoku grid.
     *
     * @param sudokuGrid the Sudoku grid
     */
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

    /**
     * Solves the Sudoku puzzle using a SudokuSolver.
     */
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

    /**
     * Checks if the game is finished (i.e., all cells are filled and the board is valid).
     *
     * @return true if the game is finished, false otherwise
     */
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

    /**
     * Undoes the last move in the game history.
     */
    public void undo() {
        Move lastMove = moveHistory.undo();
        if (lastMove != null) {
            board.setCellValue(lastMove.getRow(), lastMove.getCol(), 0);
            System.out.println("Undo completed");
        } else {
            System.out.println("No moves to undo.");
        }
    }

    /**
     * Redoes the last undone move in the game history.
     */
    public void redo() {
        Move lastUndoneMove = moveHistory.redo();
        if (lastUndoneMove != null) {
            board.setCellValue(lastUndoneMove.getRow(), lastUndoneMove.getCol(), lastUndoneMove.getValue());
            System.out.println("Redo completed");
        } else {
            System.out.println("No moves to redo.");
        }
    }
}