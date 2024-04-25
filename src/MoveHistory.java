import java.util.ArrayList;
import java.util.List;

public class MoveHistory {
    private List<Move> moves;
    private List<Move> undoneMoves;

    public MoveHistory() {
        moves = new ArrayList<>();
        undoneMoves = new ArrayList<>();
    }

    public void addMove(Move move) {
        moves.add(move);
        undoneMoves.clear(); // Clear undone moves when a new move is made
    }

    public Move undo() {
        if (moves.isEmpty()) {
            return null;
        }
        Move lastMove = moves.remove(moves.size() - 1);
        undoneMoves.add(lastMove);
        return lastMove;
    }

    public Move redo() {
        if (undoneMoves.isEmpty()) {
            return null;
        }
        Move lastUndoneMove = undoneMoves.remove(undoneMoves.size() - 1);
        moves.add(lastUndoneMove);
        return lastUndoneMove;
    }

    public boolean canUndo() {
        return !moves.isEmpty();
    }

    public boolean canRedo() {
        return !undoneMoves.isEmpty();
    }
}