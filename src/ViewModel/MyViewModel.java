package ViewModel;

import Model.IModel;
import Model.MyModel;
import View.IView;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

public class MyViewModel extends Observable implements Observer {
    IModel model;

    public MyViewModel(IModel model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            setChanged();
            notifyObservers();
        }
    }

    public void generateMaze(int width, int height,Boolean newGame) {
        model.getMaze(width, height,newGame);
    }

    public void moveCharacter(KeyCode movement) {
        model.moveCharacter(movement);
    }

    public int[][] getMaze() {
        return model.getMaze();
    }

    public int getCharacterPositionRow() {
        return model.getCharacterPositionRow();
    }

    public int getCharacterPositionColumn() {
        return model.getCharacterPositionColumn();
    }

    public int getGoalRow() {
        return model.getGoalRow();
    }

    public int getGoalColumn() {
        return model.getGoalColumn();
    }

    public Solution getSolution() {
        return model.MazeSolution();
    }

    public void saveGame(File f) {
        model.saveALL(f);
    }

    public void loadGame(File selectedFile) {
        model.Load(selectedFile);
    }

    public int getRows() {
        return model.getNumRow();
    }
    public int getCOl() {
        return model.getNumCol();
    }
}
