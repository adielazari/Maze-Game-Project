package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;

public interface IModel {
    int[][] getMaze(int rowSize , int colunmSize,Boolean newGame) ;
    int[][] getMaze();
    Solution MazeSolution();
    void ServerClose();
    void moveCharacter(KeyCode movement);
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    int getGoalRow();
    int getGoalColumn();
    void saveALL(File f);
    void Load(File file);
    int getNumRow();
    int getNumCol();

}
