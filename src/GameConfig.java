public class GameConfig {
    private int size;
    private int difficulty;
    private boolean playWithTimer;

    public GameConfig(int size, int difficulty, boolean playWithTimer) {
        this.size = size;
        this.difficulty = difficulty;
        this.playWithTimer = playWithTimer;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isPlayWithTimer() {
        return playWithTimer;
    }

    public void setPlayWithTimer(boolean playWithTimer) {
        this.playWithTimer = playWithTimer;
    }
}