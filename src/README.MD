# Sudoku Game Readme
This readme will guide you through the structure and functionality of the Sudoku Game code, as well as how to run it.



## Java Installation Guide
This guide will help you install Java on your system if it is not already installed.

### Windows
Go to the Oracle Java SE Downloads page (https://www.oracle.com/java/technologies/downloads/).

Under **Java SE Development Kit**, click the **JDK Download** button for the latest version of Java.

Accept the license agreement and click the download link for your system (Windows x64).

Once the download is complete, run the installer and follow the prompts to install Java.
Add the Java bin directory to your system's ```PATH``` environment variable. To do this, follow these steps:

Right-click on **Computer** and choose **Properties**.

Click **Advanced system settings**.

Click ```Environment Variables```.
Under ```System variables```, find the ```Path``` variable and click **Edit**.

Add the path to the Java bin directory (e.g., C:\Program Files\Java\jdk-17.0.1\bin) to the end of the variable value, separated by a semicolon (;).

Click **OK** to close all dialog boxes.

Open a new command prompt and type java -version to verify that Java is installed and added to the ```PATH```.

### macOS
Go to the Oracle Java SE Downloads page (https://www.oracle.com/java/technologies/downloads/).

Under **Java SE Development Kit**, click the **JDK Download** button for the latest version of Java.

Accept the license agreement and click the download link for your system (macOS x64).

Once the download is complete, open the ```.dmg``` file and follow the prompts to install Java.

Add the Java bin directory to your system's ```PATH``` environment variable. To do this, follow these steps:

Open a terminal window.
Type 
```bash
nano ~/.bash_profile
```
to open the.bash_profile file in a text editor.

Add the following line to the end of the file: 

```bash
export PATH=/Library/Java/JavaVirtualMachines/jdk-17.0.1.jdk/Contents/Home/bin:$PATH
```
Save and close the file.

Type 
```bash
source ~/.bash_profile
```
to reload the ```.bash_profile``` file. 

Open a new terminal window and type
```bash
java -version
```
to verify that Java is installed and added to the ```PATH```.

### Linux
Go to the Oracle Java SE Downloads page (https://www.oracle.com/java/technologies/downloads/).

Under **Java SE Development Kit**, click the **JDK Download** button for the latest version of Java.

Accept the license agreement and click the download link for your system (Linux x64).

Extract the.tar.gz file to a directory of your choice.

Add the Java bin directory to your system's ```PATH``` environment variable. To do this, follow these steps:

Open a terminal window.
Type #
```bash
nano ~/.bashrc
```
to open the ```.bashrc``` file in a text editor.

Add the following line to the end of the file: 
```bash
export PATH=/path/to/jdk-17.0.1/bin:$PATH
```
Save and close the file.
Type 
```bash
source ~/.bashrc
```
to reload the ```.bashrc``` file.

Open a new terminal window and type 
```bash
java -version
```
to verify that Java is installed and added to the ```PATH```.

Once Java is installed and added to the ```PATH```, you can compile and run the Sudoku Game code as described in the previous response.

## Code Structure
The Sudoku Game code consists of several Java classes, each with a specific role:

* Main.java: This is the main class that runs the game. It creates a new SudokuGame object and starts the game loop.
* GameConfig.java: This class represents the game configuration settings, such as the board size and difficulty level.
* GameUtils.java: This class contains utility methods for the game, such as closing the scanner.
* Move.java: This class represents a single move in the game, storing the row, column, and value of the move.
* MoveHistory.java: This class keeps track of the moves made during the game and allows undoing and redoing moves.
* Replay.java: This class represents a replay of a game, storing the initial board state, moves, and elapsed time.
* SudokuBoard.java: This class represents the Sudoku board, handling cell values and move validation.
* SudokuGame.java: This is the main game logic class, handling user input, game state, and game loop.
* SudokuGenerator.java: This class generates a Sudoku puzzle with a given difficulty level.
* SudokuSolver.java: This class solves a Sudoku puzzle using a backtracking algorithm.
* Timer.java: This class tracks elapsed time when playing with a timer.

## Running the Code
To run the Sudoku Game code, follow these steps:

Make sure you have Java installed on your system.

Compile the Java classes using the following command in the terminal:

```bash
javac *.java
```

Run the Main class using the following command:

```bash
java Main
```
Follow the prompts in the console to configure and play the game.

## Game Configuration
When starting a new game, you will be prompted to choose a difficulty level (easy, medium, or hard) and whether to play with a timer. The game configuration settings are stored in the GameConfig object.

Game Loop
The game loop handles user input, game state, and game logic. The loop continues until the game is finished or the user quits. The following actions are available during the game loop:
```
Enter a move in the format 'row column value'.
Enter 'solve' to solve the puzzle.
Enter 'q' to quit the game.
Enter 'u' to undo the last move.
Enter 'r' to redo the last undone move.
Saving and Loading Replays
The game allows you to save and load replays of games. To save a replay, choose the 'Save replay' option in the main menu and enter a filename. To load a replay, choose the 'Load and replay saved game' option in the main menu and enter the filename of the saved replay.
```

## Timer
If you choose to play with a timer, the elapsed time will be displayed above the board during the game. The timer can be stopped and started using the 't' key. The elapsed time is saved with the replay data.

## License

[MIT](https://choosealicense.com/licenses/mit/)