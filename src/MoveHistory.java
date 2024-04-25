import java.util.ArrayList;
import java.util.List;
/**
 * Represents a history of moves made in a game.
 */
public class MoveHistory {
    private List<Move> moves;
    private List<Move> undoneMoves;
    /**
     * Constructs a new MoveHistory object.
     */
    public MoveHistory() {
        moves = new ArrayList<>();
        undoneMoves = new ArrayList<>();
    }
    /**
     * Adds a new move to the history.
     *
     * @param move the move to add
     */
    public void addMove(Move move) {
        moves.add(move);
        undoneMoves.clear(); // Clear undone moves when a new move is made
    }
    /**
     * Undoes the last move in the history.
     *
     * @return the undone move, or null if there are no moves to undo
     */
    public Move undo() {
        if (moves.isEmpty()) {
            return null;
        }
        Move lastMove = moves.remove(moves.size() - 1);
        undoneMoves.add(lastMove);
        return lastMove;
    }
    /**
     * Redoes the last undone move in the history.
     *
     * @return the redone move, or null if there are no moves to redo
     */
    public Move redo() {
        if (undoneMoves.isEmpty()) {
            return null;
        }
        Move lastUndoneMove = undoneMoves.remove(undoneMoves.size() - 1);
        moves.add(lastUndoneMove);
        return lastUndoneMove;
    }
    /**
     * Checks if there are any moves that can be undone.
     *
     * @return true if there are moves to undo, false otherwise
     */
    public boolean canUndo() {
        return !moves.isEmpty();
    }
    /**
     * Checks if there are any moves that can be redone.
     *
     * @return true if there are moves to redo, false otherwise
     */
    public boolean canRedo() {
        return !undoneMoves.isEmpty();
    }
}