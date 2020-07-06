package View;

import Model.MyMazePlayerData;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.net.URL;

/**
 * Created by Aviadjo on 3/9/2017.
 */
public class MazeDisplayer extends Canvas {

    private StringProperty someProperty;

    public MazeDisplayer(double width, double height) {
        super(width, height);
        this.canvasHeight = width;
        this.canvasWidth = height;
        init();
    }


    public String getSomeProperty() {
        return someProperty.get();
    }

    public void setSomeProperty(String someProperty) {
        this.someProperty.set(someProperty);
    }


    final static int backgroundCode = 0;
    final static int wallCode = 1;
    final static int pathCode = 2;
    final static int emptyCode = 3;
    final static int visitedCode = 4;

    double canvasHeight ;
    double canvasWidth ;
    double cellHeight ;
    double cellWidth ;

    private int rows;
    private int columns;
    Image playerLocationImage , Wallimage, inputEndPointImage;

    Boolean RestoreFromFile;

    private MyMazePlayerData MazeData;
  //  Canvas canvas;      // the canvas where the maze is drawn and which fills the whole window
    public GraphicsContext graphicsContext2D;  // graphics context for drawing on the canvas

    Color[] color;          // colors associated with the preceding 5 constants;

    Pair<Integer , Integer> correntLocation ;
    public  Pair<Integer , Integer>  getCorrentLocation(){
        return correntLocation ;
    }
//
    public MazeDisplayer() {
        super();
        init();

    }
    private void init(){
        zooming=false;
        color = new Color[] {
                Color.rgb(200,180,0),
                Color.rgb(200,0,0),
                Color.rgb(255,91,32),
                Color.WHITE,
                Color.rgb(200,200,200)
        };
                System.out.println(playerLocationImage==null);
        try{
            URL inputPic = MazeDisplayer.class.getClassLoader().getResource("Images/jerry.jpg");
            playerLocationImage = new Image(inputPic.toString() , false);
            URL input = MazeDisplayer.class.getClassLoader().getResource("Images/wall3.jpg");
            Wallimage = new Image( input.toString(), false);
            URL inputEndPoint = MazeDisplayer.class.getClassLoader().getResource("Images/Tom.jpg");
            inputEndPointImage = new Image(inputEndPoint.toString() , false);
        }catch (Exception e){

        }
        RestoreFromFile = false;
        widthProperty().addListener(evt -> ReSizeMze());
        heightProperty().addListener(evt -> ReSizeMze());

    }
private int x;
    public void displaySettings(int rows , int columns){

        synchronized (this) {
            this.rows = rows;
            this.columns = columns;
       //     if(!zooming) {
                x = 50;
                canvasHeight = getHeight() ; /////////////////////////////////////////////////////////////// need to be fixed !
                canvasWidth = getWidth() ;
       //     }
            System.out.println(getHeight());
            cellHeight = canvasHeight / rows;
            cellWidth = canvasWidth / columns;
            graphicsContext2D = getGraphicsContext2D();

            System.out.println(canvasHeight + "," + canvasWidth + "," + cellHeight + "," + cellWidth);
            graphicsContext2D.clearRect(0, 0, canvasWidth, canvasHeight); //Clean the Canvas
        }

    }
    private boolean zooming ;
    public void ReSizeMze() {
     //   zooming = false;
        displaySettings(rows, columns);
        redraw();
    }
    public void ReSizeMze(double zoom){
        widthProperty().set(widthProperty().getValue()*zoom);
        heightProperty().set(heightProperty().getValue()*zoom);
        displaySettings(rows, columns);
        redraw();
    }

    int [][] maze;
    private int characterPositionRow = -1;
    private int characterPositionColumn = 1;
    private int goalRow = -1;
    private int goalColumn =-1;
    public void setMaze(int[][] maze ,int  goalRow , int goalColumn) {
        this.maze = maze;
        this.goalRow=goalRow;
        this.goalColumn=goalColumn;
        redraw();
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }
    public void printSolution(Solution sol){
        for (AState state: sol.getSolutionPath() ) {
                pathPaint(((MazeState) state).getRow() , ((MazeState) state).getColum());
            if(characterPositionColumn != -1 && characterPositionRow!= -1) {
                pathPaint(characterPositionRow, characterPositionColumn);
            }
        }
    }
    public void redraw() {
        if (maze != null) {
            graphicsContext2D.clearRect( 0 ,0 , canvasWidth , canvasHeight+x);
            try {

                //Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            printToGraphicsMaze(i,j); // wall printing
                        }
                    }
                }

                //Draw Character
                if(characterPositionColumn != -1 && characterPositionRow!= -1) {
                    pathPaint(characterPositionRow, characterPositionColumn);
                }
                if(goalColumn != -1 && goalRow !=-1) {
                    printEndPointToGraphicsMaze (goalRow, goalColumn);
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    public void printStartPointToGraphicsMaze(int row , int column){
        graphicsContext2D.setFill( color[pathCode] );
        fillIt(row , column);
    }

    public void printEndPointToGraphicsMaze(int row , int column){
//        graphicsContext2D.setFill( color[4] );
//        fillIt(row , column);
        graphicsContext2D.drawImage(inputEndPointImage, column*cellWidth ,row*cellHeight, cellWidth, cellHeight);
    }

    public void pathPaint (int row , int column){
     //   graphicsContext2D.clearRect( characterPositionColumn*cellWidth ,characterPositionRow*cellHeight , cellWidth, cellHeight);
            LocationPicture(column*cellWidth, row*cellHeight);
    }

    private void LocationPicture(double columnPixel , double rowPixel ){
        if (!RestoreFromFile) {
            if (characterPositionColumn != -1 && characterPositionRow != -1 ) {
                graphicsContext2D.setFill(color[pathCode]);
                graphicsContext2D.clearRect( characterPositionColumn*cellWidth ,characterPositionRow*cellHeight , cellWidth, cellHeight);
            //    graphicsContext2D.fillRect(characterPositionColumn*cellWidth ,characterPositionRow*cellHeight , cellWidth, cellHeight);
            }
            graphicsContext2D.drawImage(playerLocationImage, columnPixel, rowPixel, cellWidth, cellHeight);
        }
    }

    public void printToGraphicsMaze(int row, int column) {
        graphicsContext2D.drawImage(Wallimage, column*cellWidth ,row*cellHeight, cellWidth, cellHeight);
    }

    private void fillIt(int row , int column ){
        double x = cellHeight * row;
        double y = cellWidth * column;
        graphicsContext2D.fillRect(y, x,  cellWidth ,cellHeight);
    }
}
