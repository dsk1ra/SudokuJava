/**
 * Represents a configuration object for a Sudoku game.
 */
public class GameConfig {
    private int size;
    private int difficulty;
    private boolean playWithTimer;

    /**
     * Constructs a new GameConfig object with a given size, difficulty, and playWithTimer flag.
     *
     * @param size          the size of the Sudoku puzzle
     * @param difficulty    the difficulty level of the Sudoku puzzle
     * @param playWithTimer whether to play the game with a timer
     */
    public GameConfig(int size, int difficulty, boolean playWithTimer) {
        this.size = size;
        this.difficulty = difficulty;
        this.playWithTimer = playWithTimer;
    }

    /**
     * Returns the size of the Sudoku puzzle.
     *
     * @return the size of the Sudoku puzzle
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size of the Sudoku puzzle.
     *
     * @param size the new size of the Sudoku puzzle
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Returns the difficulty level of the Sudoku puzzle.
     *
     * @return the difficulty level of the Sudoku puzzle
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level of the Sudoku puzzle.
     *
     * @param difficulty the new difficulty level of the Sudoku puzzle
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Returns whether to play the game with a timer.
     *
     * @return true if the game should be played with a timer, false otherwise
     */
    public boolean isPlayWithTimer() {
        return playWithTimer;
    }

    /**
     * Sets whether to play the game with a timer.
     *
     * @param playWithTimer the new playWithTimer flag
     */
    public void setPlayWithTimer(boolean playWithTimer) {
        this.playWithTimer = playWithTimer;
    }
}