package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;

public interface IView {
//    void printToGraphicsMaze(int row , int column);
//    void printStartPointToGraphicsMaze(int row , int column);
//    void printEndPointToGraphicsMaze(int row , int column);
//    void pathPaint (int row , int column);
      void displayMaze(int row, int col, int[][] maze , int goalRow , int goalColumn);
}
