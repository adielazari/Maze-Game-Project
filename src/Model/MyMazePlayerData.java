package Model;

import algorithms.mazeGenerators.Maze;
import javafx.util.Pair;

import java.io.Serializable;

public class MyMazePlayerData implements Serializable {
    private   int[][] maze ;
    private int goalRow;
    private int goalColumn;
    private int characterPositionColumn;
    private  int characterPositionRow;
    private Maze mazeR;
    private int numRow;
    private int numCol;

    public MyMazePlayerData(int[][] maze, int goalRow, int goalColumn, int characterPositionColumn, int characterPositionRow, Maze mazeR, int numRow, int numCol) {
        this.maze = maze;
        this.goalRow = goalRow;
        this.goalColumn = goalColumn;
        this.characterPositionColumn = characterPositionColumn;
        this.characterPositionRow = characterPositionRow;
        this.mazeR = mazeR;
        this.numRow=numRow;
        this.numCol=numCol;
    }

    public int[][] getMaze() {
        return maze;
    }

    public int getGoalRow() {
        return goalRow;
    }

    public int getGoalColumn() {
        return goalColumn;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public Maze getMazeR() {
        return mazeR;
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
    }

    public void setGoalRow(int goalRow) {
        this.goalRow = goalRow;
    }

    public void setGoalColumn(int goalColumn) {
        this.goalColumn = goalColumn;
    }

    public void setCharacterPositionColumn(int characterPositionColumn) {
        this.characterPositionColumn = characterPositionColumn;
    }

    public void setCharacterPositionRow(int characterPositionRow) {
        this.characterPositionRow = characterPositionRow;
    }

    public int getNumRow() {
        return numRow;
    }

    public int getNumCol() {
        return numCol;
    }

    public void setMazeR(Maze mazeR) {
        this.mazeR = mazeR;
    }
}
