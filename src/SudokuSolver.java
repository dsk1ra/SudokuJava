public class SudokuSolver {
    private int[][] board;
    private int size;

    public SudokuSolver(int[][] board) {
        this.board = board;
        this.size = board.length;
    }

    public int[][] solve() {
        if (solveSudoku()) {
            return board;
        } else {
            System.out.println("No solution exists for the given Sudoku puzzle.");
            return null;
        }
    }

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

    private boolean isValidMove(int row, int col, int num) {
        return isRowValid(row, num) && isColValid(col, num) && isSubgridValid(row, col, num);
    }

    private boolean isRowValid(int row, int num) {
        for (int col = 0; col < size; col++) {
            if (board[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    private boolean isColValid(int col, int num) {
        for (int row = 0; row < size; row++) {
            if (board[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    private boolean isSubgridValid(int row, int col, int num) {
        int subgridSize = (int) Math.sqrt(size);
        int startRow = row - row % subgridSize;
        int startCol = col - col % subgridSize;
        for (int i = 0; i < subgridSize; i++) {
            for (int j = 0; j < subgridSize; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }
}
