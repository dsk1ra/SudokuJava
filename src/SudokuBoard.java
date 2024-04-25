public class SudokuBoard {
    private int[][] board;
    private final int size; // Size of the Sudoku grid (e.g., 9 for standard Sudoku)

    public SudokuBoard(int size) {
        this.size = size;
        this.board = new int[size][size];
        // Initialize the board with zeros (empty cells)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;
            }
        }
    }

    // Get the value of a cell in the board
    public int getCellValue(int row, int col) {
        return board[row][col];
    }

    // Set the value of a cell in the board
    public void setCellValue(int row, int col, int value) {
        if (!isValidCell(row, col) || !isValidValue(value)) {
            throw new IllegalArgumentException("Invalid cell or value");
        }
        board[row][col] = value;
    }

    // Check if a cell coordinate is valid
    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    // Check if a value is valid for a cell
    private boolean isValidValue(int value) {
        return value >= 0 && value <= size;
    }

    // Check if a move is valid
    public boolean isValidMove(int row, int col, int value) {
        if (!isValidCell(row, col) || !isValidValue(value)) {
            return false;
        }

        // Check row, column, and subgrid
        return !isValueInRow(row, value) &&
                !isValueInColumn(col, value) &&
                !isValueInSubgrid(row - row % 3, col - col % 3, value);
    }

    // Check if a value exists in the row
    private boolean isValueInRow(int row, int value) {
        for (int col = 0; col < size; col++) {
            if (board[row][col] == value) {
                return true;
            }
        }
        return false;
    }

    // Check if a value exists in the column
    private boolean isValueInColumn(int col, int value) {
        for (int row = 0; row < size; row++) {
            if (board[row][col] == value) {
                return true;
            }
        }
        return false;
    }

    // Check if a value exists in the 3x3 subgrid
    private boolean isValueInSubgrid(int startRow, int startCol, int value) {
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                if (board[row][col] == value) {
                    return true;
                }
            }
        }
        return false;
    }

    // Get the size of the board
    public int getSize() {
        return size;
    }

    public int[][] getBoard() {
        int[][] copy = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    // Method to set the entire board
    public void setBoard(int[][] newBoard) {
        if (newBoard.length == size && newBoard[0].length == size) {
            this.board = newBoard;
        } else {
            System.out.println("Invalid board size. The size of the new board must match the current board size.");
        }
    }

    // Method to print the board
    public void printBoard(boolean[][] generatedCells) {
        // Print column numbers
        System.out.print("  ");
        for (int i = 0; i < size; i++) {
            System.out.print(i + " ");
            if ((i + 1) % 3 == 0 && i < size - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();

        // Print horizontal line
        System.out.print(" ");
        for (int i = 0; i < size + (size / 3 - 1); i++) {
            System.out.print("--");
        }
        System.out.println();

        // Print board contents
        for (int i = 0; i < size; i++) {
            // Print row number
            System.out.print(i + "|");

            for (int j = 0; j < size; j++) {
                if (generatedCells[i][j]) { // Generated cell
                    System.out.print("\u001B[34m" + board[i][j] + " " + "\u001B[0m"); // Blue color
                } else { // User-input cell
                    if (board[i][j] == 0) { // Check if the cell value is zero
                        System.out.print("  "); // Print a blank space
                    } else {
                        System.out.print("\u001B[32m" + board[i][j] + " " + "\u001B[0m"); // Green color
                    }
                }

                if ((j + 1) % 3 == 0 && j < size - 1) {
                    System.out.print("| ");
                }
            }
            System.out.println();

            // Print horizontal line after every 3 rows
            if ((i + 1) % 3 == 0 && i < size - 1) {
                System.out.print(" ");
                for (int j = 0; j < size + (size / 3 - 1); j++) {
                    System.out.print("--");
                }
                System.out.println();
            }
        }
    }
}
