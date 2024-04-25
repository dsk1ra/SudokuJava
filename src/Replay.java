import java.util.ArrayList;
import java.util.List;

/**
 * Represents a replay of a Sudoku game.
 */
public class Replay {
    private final int[][] board;
    private final List<Move> moves;
    private final String elapsedTime;

    /**
     * Constructs a new Replay object with a given board, moves, and elapsed time.
     *
     * @param board       the Sudoku puzzle board
     * @param moves       the list of moves made during the game
     * @param elapsedTime the elapsed time of the game
     */
    public Replay(int[][] board, List<Move> moves, String elapsedTime) {
        this.board = board;
        this.moves = new ArrayList<>(moves);
        this.elapsedTime = elapsedTime;
    }

    /**
     * Constructs a new Replay object with a given list of moves and elapsed time.
     *
     * @param moves       the list of moves made during the game
     * @param elapsedTime the elapsed time of the game
     */
    public Replay(List<Move> moves, String elapsedTime) {
        this(new int[9][9], moves, elapsedTime);
    }

    /**
     * Returns the Sudoku board.
     *
     * @return the Sudoku board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Returns the list of moves made during the game.
     *
     * @return the list of moves made during the game
     */
    public List<Move> getMoves() {
        return moves;
    }

    /**
     * Returns the elapsed time of the game.
     *
     * @return the elapsed time of the game
     */
    public String getElapsedTime() {
        return elapsedTime;
    }
}