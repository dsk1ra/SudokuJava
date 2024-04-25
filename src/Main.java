
public class Main {
    public static void main(String[] args) {
        GameConfig config = new GameConfig(9, 40, true); // 9 for standard Sudoku, true to play with timer
        SudokuGame game = new SudokuGame(config); // 9 for standard Sudoku
        game.startGame();
    }
}