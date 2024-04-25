/**
 * Represents a Sudoku solver that can solve a given Sudoku puzzle.
 */
public class SudokuSolver {
    final private int[][] board;
    final private int size;
    /**
     * Constructs a new SudokuSolver object with a given Sudoku puzzle board.
     *
     * @param board the Sudoku puzzle board
     */
    public SudokuSolver(int[][] board) {
        this.board = board;
        this.size = board.length;
    }
    /**
     * Solves the Sudoku puzzle and returns the solved board.
     *
     * @return the solved Sudoku puzzle board, or null if no solution exists
     */
    public int[][] solve() {
        if (solveSudoku()) {
            return board;
        } else {
            System.out.println("No solution exists for the given Sudoku puzzle.");
            return null;
        }
    }
    /**
     * Recursive helper method for solving the Sudoku puzzle.
     *
     * @return true if the puzzle is solved, false otherwise
     */
    private boolean solveSudoku() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= size; num++) {
                        if (isValidMove(row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku()) {
                                return true;
                            }
                            board[row][col] = 0; // Backtrack
                        }
                    }
                    return false; // No valid number found for this cell
                }
            }
        }
        return true; // Puzzle solved
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
            if (board[row][col] == num) {
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if a given number is valid in a given column.
     *
     * @param col the row
     * @param num the number to check
     * @return true if the number is valid, false otherwise
     */
    private boolean isColValid(int col, int num) {
        for (int row = 0; row < size; row++) {
            if (board[row][col] == num) {
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
        return SudokuGenerator.getSubgridSize(row, col, num, size, board);
    }
}
