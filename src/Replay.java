import java.util.ArrayList;
import java.util.List;

public class Replay {
    private final int[][] board;
    private final List<Move> moves;
    private final String elapsedTime;

    public Replay(int[][] board, List<Move> moves, String elapsedTime) {
        this.board = board;
        this.moves = new ArrayList<>(moves);
        this.elapsedTime = elapsedTime;
    }

    public Replay(List<Move> moves, String elapsedTime) {
        this(new int[9][9], moves, elapsedTime);
    }

    public int[][] getBoard() {
        return board;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }
}