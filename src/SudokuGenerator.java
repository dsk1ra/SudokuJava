import java.util.Random;

/**
 * Generates a Sudoku puzzle with a given difficulty level.
 */
public class SudokuGenerator {
    private final int[][] grid;
    private final int size;
    private final Random random;

    /**
     * Constructs a new SudokuGenerator object with a given GameConfig object.
     *
     * @param config the game configuration
     */
    public SudokuGenerator(GameConfig config) {
        this.size = config.getSize();
        this.grid = new int[size][size];
        this.random = new Random();
        generateSudoku(config.getDifficulty());
    }

    /**
     * Generates a Sudoku puzzle with the given difficulty level.
     *
     * @param difficulty the difficulty level (1-3) or a custom value
     */
    private void generateSudoku(int difficulty) {
        fillDiagonalSubgrid();
        solveSudoku();
        int filledCellsPercentage;
        if (difficulty < 0) {
            filledCellsPercentage = -difficulty; // Custom difficulty, use the absolute value
        } else {
            filledCellsPercentage = switch (difficulty) {
                case 1 -> 50;
                case 2 -> 40;
                case 3 -> 30;
                default -> 50; // Default to easy difficulty
            };
        }
        removeNumbers(filledCellsPercentage);
    }

    /**
     * Fills the diagonal subgrids of the Sudoku puzzle.
     */

    private void fillDiagonalSubgrid() {
        int subgridSize = (int) Math.sqrt(size);
        for (int i = 0; i < size; i += subgridSize) {
            fillSubgrid(i, i);
        }
    }

    /**
     * Fills a single subgrid of the Sudoku puzzle.
     *
     * @param startRow the starting row of the subgrid
     * @param startCol the starting column of the subgrid
     */
    private void fillSubgrid(int startRow, int startCol) {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        shuffleArray(numbers);
        int idx = 0;
        int subgridSize = (int) Math.sqrt(size);
        for (int i = 0; i < subgridSize; i++) {
            for (int j = 0; j < subgridSize; j++) {
                grid[startRow + i][startCol + j] = numbers[idx++];
            }
        }
    }

    /**
     * Solves the Sudoku puzzle using a recursive backtracking algorithm.
     */
    private void solveSudoku() {
        solveSudoku(0, 0);
    }

    /**
     * Recursive helper method for solving the Sudoku puzzle.
     *
     * @param row the current row
     * @param col the current column
     * @return true if the puzzle is solved, false otherwise
     */
    private boolean solveSudoku(int row, int col) {
        if (row == size) {
            return true; // Puzzle solved
        }

        if (col == size) {
            return solveSudoku(row + 1, 0); // Move to the next row
        }

        if (grid[row][col] != 0) {
            return solveSudoku(row, col + 1); // Skip filled cells
        }

        for (int num = 1; num <= size; num++) {
            if (isValidMove(row, col, num)) {
                grid[row][col] = num;
                if (solveSudoku(row, col + 1)) {
                    return true;
                }
                grid[row][col] = 0; // Backtrack
            }
        }

        return false; // No valid number found
    }

    /**
     * Checks if a given number is valid in a given position.
     *
     * @param row the row
     * @param col the column
     * @param num the number to check
     * @return true if the number is valid, false otherwise
     */
    private boolean isValidMove(int row, int col, int num) {
        return isRowValid(row, num) && isColValid(col, num) && isSubgridValid(row, col, num);
    }

    /**
     * Checks if a given number is valid in a given row.
     *
     * @param row the row
     * @param num the number to check
     * @return true if the number is valid, false otherwise
     */
    private boolean isRowValid(int row, int num) {
        for (int col = 0; col < size; col++) {
            if (grid[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a given number is valid in a given column.
     *
     * @param col the column
     * @param num the number to check
     * @return true if the number is valid, false otherwise
     */
    private boolean isColValid(int col, int num) {
        for (int row = 0; row < size; row++) {
            if (grid[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a given number is valid in a given subgrid.
     *
     * @param row the row
     * @param col the column
     * @param num the number to check
     * @return true if the number is valid, false otherwise
     */
    private boolean isSubgridValid(int row, int col, int num) {
        return getSubgridSize(row, col, num, size, grid);
    }
    /**
     * Checks if a given number is valid in a given subgrid.
     *
     * @param row the row
     * @param col the column
     * @param num the number to check
     * @param size the size of the Sudoku puzzle
     * @param grid the Sudoku puzzle grid
     * @return true if the number is valid in the subgrid, false otherwise
     */
    static boolean getSubgridSize(int row, int col, int num, int size, int[][] grid) {
        int subgridSize = (int) Math.sqrt(size);
        int startRow = row - row % subgridSize;
        int startCol = col - col % subgridSize;
        for (int i = 0; i < subgridSize; i++) {
            for (int j = 0; j < subgridSize; j++) {
                if (grid[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Shuffles an array of numbers.
     *
     * @param array the array to shuffle
     */
    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int idx = random.nextInt(i + 1);
            int temp = array[idx];
            array[idx] = array[i];
            array[i] = temp;
        }
    }
    /**
     * Removes numbers from the Sudoku puzzle to create a solvable puzzle.
     *
     * @param filledCellsPercentage the percentage of cells to fill
     */
    private void removeNumbers(int filledCellsPercentage) {
        int cellsToRemove = (size * size * (100 - filledCellsPercentage)) / 100;
        while (cellsToRemove > 0) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            if (grid[row][col] != 0) {
                grid[row][col] = 0;
                cellsToRemove--;
            }
        }
    }
    /**
     * Returns the generated Sudoku puzzle as a 2D array.
     *
     * @return the Sudoku puzzle
     */
    public int[][] getSudokuGrid() {
        return grid;
    }
}
