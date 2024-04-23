import java.util.Random;

public class SudokuGenerator {
    private final int[][] grid;
    private final int size;
    private final Random random;

    public SudokuGenerator(int size, int difficulty) {
        this.size = size;
        this.grid = new int[size][size];
        this.random = new Random();
        generateSudoku(difficulty);
    }

    private void generateSudoku(int difficulty) {
        fillDiagonalSubgrid();
        solveSudoku();
        int filledCellsPercentage = switch (difficulty) {
            case 1 -> 50;
            case 2 -> 40;
            case 3 -> 30;
            default -> 50; // Default to easy difficulty
        };
        removeNumbers(filledCellsPercentage);
    }


    private void fillDiagonalSubgrid() {
        int subgridSize = (int) Math.sqrt(size);
        for (int i = 0; i < size; i += subgridSize) {
            fillSubgrid(i, i);
        }
    }

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

    private void solveSudoku() {
        solveSudoku(0, 0);
    }

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

    private boolean isValidMove(int row, int col, int num) {
        return isRowValid(row, num) && isColValid(col, num) && isSubgridValid(row, col, num);
    }

    private boolean isRowValid(int row, int num) {
        for (int col = 0; col < size; col++) {
            if (grid[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    private boolean isColValid(int col, int num) {
        for (int row = 0; row < size; row++) {
            if (grid[row][col] == num) {
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
                if (grid[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int idx = random.nextInt(i + 1);
            int temp = array[idx];
            array[idx] = array[i];
            array[i] = temp;
        }
    }

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


    public int[][] getSudokuGrid() {
        return grid;
    }

    public static void main(String[] args) {
        int size = 9; // Default size for a 9x9 Sudoku puzzle
        int difficulty = 1; // Default difficulty (you can change this as needed)
        SudokuGenerator generator = new SudokuGenerator(size, difficulty);
        int[][] sudokuGrid = generator.getSudokuGrid();

        // Display the generated Sudoku grid
        System.out.println("Generated Sudoku Grid:");
        printSudokuGrid(sudokuGrid);
    }


    private static void printSudokuGrid(int[][] sudokuGrid) {
        for (int i = 0; i < sudokuGrid.length; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("---------------------");
            }
            for (int j = 0; j < sudokuGrid[i].length; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print(sudokuGrid[i][j] + " ");
            }
            System.out.println();
        }
    }
}
