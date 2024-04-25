/**
 * Represents a move in a Sudoku game.
 */
public class Move {
    private final int row;
    private final int col;
    private final int value;

    /**
     * Constructs a new Move object with a given row, column, and value.
     *
     * @param row   the row of the cell
     * @param col   the column of the cell
     * @param value the value to place in the cell
     */
    public Move(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    /**
     * Returns the row of the cell.
     *
     * @return the row of the cell
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of the cell.
     *
     * @return the column of the cell
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the value to place in the cell.
     *
     * @return the value to place in the cell
     */
    public int getValue() {
        return value;
    }
}
