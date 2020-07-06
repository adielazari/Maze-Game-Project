package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;




public class MyModel extends Observable implements IModel {
    private static MyModel ourInstance = new MyModel();
    private static final Logger LOG = LogManager.getLogger();


    public static MyModel getInstance() {
        // org.apache.logging.log4j.core.config.Configurator;
        Configurator.setLevel("com.example.Foo", Level.DEBUG);

        // You can also set the root logger:
        Configurator.setRootLevel(Level.INFO);
        return ourInstance;
    }

    int[][] maze;
    public int[][] getMaze(){
        return maze;
    }
    Position startLocation;
    Position goalLocation;
    int numRow;
    int numCol;
    private int characterPositionRow ;
    private int characterPositionColumn ;
    private int numSave=0;
    public Position getStartLocation() {
        return startLocation;
    }
    public Position getGoalLocation() {
        return goalLocation;
    }
    

    private Server mazeGeneratingServer ;
    private Server solveSearchProblemServer;

    private MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }

    public void ServerClose(){
        if(mazeGeneratingServer!=null){
            mazeGeneratingServer.stop();
        }
        if(solveSearchProblemServer!=null) {
            solveSearchProblemServer.stop();
        }
    }
    private Maze mazeR = null;
    private void goToHome(){
        if(mazeR!=null){
            characterPositionRow = mazeR.getStartPosition().getRowIndex();
            characterPositionColumn = mazeR.getStartPosition().getColumnIndex();
        }
    }


    private int goalRow=-1;
    private MyMazePlayerData mt;
    private int goalColumn=-1;
    public int[][] getMaze(int rowSize, int colunmSize,Boolean newGame) {
        //if(!newGame) return maze;
        try {
            numRow=rowSize;
            numCol=colunmSize;
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        LOG.info("The client start generate..");
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rowSize, colunmSize};
                        LOG.info("Maze: row - "+rowSize+", column - "+ colunmSize);
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])((byte[])fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[rowSize*colunmSize+100];

                        is.read(decompressedMaze);
                        if(newGame)
                           mazeR = new Maze(decompressedMaze);
                        LOG.info("The start position is: "+ mazeR.getStartPosition());
                        LOG.info("The goal position is: "+ mazeR.getGoalPosition());
                        //   mazeR=maze;
                    } catch (Exception var10) {
                        var10.printStackTrace();
                        LOG.info("The maze not generated..");

                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
        Position[][] mazeP =  mazeR.getMatrixMaze();


         maze = new int [mazeP.length][mazeP[0].length];
        for(int i=0 ; i<mazeP.length ;i++){
            for(int j=0; j <mazeP[0].length ;j++){
                if(mazeP[i][j].isWall()){
                    maze[i][j]=1;
                }else{
                    maze[i][j]=0;
                }
            }
        }
        if(!newGame) {
            mazeR.setStartPosition(new Position(characterPositionRow,characterPositionColumn));
        }
            startLocation = mazeR.getStartPosition();
            characterPositionColumn = startLocation.getColumnIndex();
            characterPositionRow = startLocation.getRowIndex();
            goalLocation = mazeR.getGoalPosition();
            goalColumn = goalLocation.getColumnIndex();
            goalRow = goalLocation.getRowIndex();
        //}
            setChanged();
        notifyObservers();
        return maze;
    }

    Solution mazeSolution;
    public Solution MazeSolution() {
        mazeSolution = null;
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        LOG.info("The client reqiure solution..");
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(mazeR);
                        toServer.flush();
                        mazeSolution = (Solution) fromServer.readObject();
                        System.out.println("maze start:" + mazeR.getStartPosition());
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();

                        for(int i = 0; i < mazeSolutionSteps.size(); ++i) {
                            System.out.println(String.format("%s. %s", i, ((AState)mazeSolutionSteps.get(i)).toString()));
                        }
                        LOG.info("num steps is: " + mazeSolution.toString());

                    } catch (Exception var10) {
                        var10.printStackTrace();
                        LOG.info("The solution not good");

                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
        return mazeSolution;
    }
    
    private boolean CheackStep(int row, int column) {
            Position[][] p = mazeR.getMatrixMaze();
            return (!(p[row][column].isWall()));
    }


    
    public void moveCharacter(KeyCode movement){
        try {
            synchronized (this) {
                if(KeyCode.HOME == movement){
                    this.goToHome();
                }
                if (KeyCode.DIGIT8 == movement || (KeyCode.NUMPAD8 == movement)) {//up
                    if (CheackStep(characterPositionRow - 1, characterPositionColumn)) {
                        characterPositionRow--;
                    }
                }
                if (KeyCode.DIGIT2 == movement || (KeyCode.NUMPAD2 == movement)) {//down
                    if (CheackStep(characterPositionRow + 1, characterPositionColumn)) {
                        characterPositionRow++;
                    }
                }
                if (KeyCode.DIGIT4 == movement|| (KeyCode.NUMPAD4 == movement)) {//left
                    if (CheackStep(characterPositionRow, characterPositionColumn - 1)) {
                        //  pathPaint(characterPositionRow,characterPositionColumn - 1);
                        characterPositionColumn--;
                    }
                }
                if (KeyCode.DIGIT6 == movement || (KeyCode.NUMPAD6 == movement)) {//right
                    if (CheackStep(characterPositionRow, characterPositionColumn + 1)) {
                        characterPositionColumn++;
                    }
                }


                if (KeyCode.DIGIT9 == movement || (KeyCode.NUMPAD9 == movement)) {//up right
                    if (CheackStep(characterPositionRow - 1, characterPositionColumn)
                            && (CheackStep(characterPositionRow - 1, characterPositionColumn + 1))
                            || (CheackStep(characterPositionRow, characterPositionColumn + 1)
                            && (CheackStep(characterPositionRow - 1, characterPositionColumn + 1)))) {

                        characterPositionColumn++;
                        characterPositionRow--;
                    }
                }
                if (KeyCode.DIGIT3 == movement || (KeyCode.NUMPAD3 == movement)) {//down right
                    if (CheackStep(characterPositionRow + 1, characterPositionColumn)
                            && (CheackStep(characterPositionRow + 1, characterPositionColumn + 1))
                            || (CheackStep(characterPositionRow, characterPositionColumn + 1)
                            && (CheackStep(characterPositionRow + 1, characterPositionColumn + 1)))) {
                        characterPositionColumn++;
                        characterPositionRow++;
                    }
                }
                if (KeyCode.DIGIT7 == movement || (KeyCode.NUMPAD7 == movement)) {//up left
                    if (CheackStep(characterPositionRow - 1, characterPositionColumn)
                            && (CheackStep(characterPositionRow - 1, characterPositionColumn - 1))
                            || (CheackStep(characterPositionRow, characterPositionColumn - 1)
                            && (CheackStep(characterPositionRow - 1, characterPositionColumn - 1)))) {
                        characterPositionRow--;
                        characterPositionColumn--;
                    }
                }
                if (KeyCode.DIGIT1 == movement || (KeyCode.NUMPAD1 == movement)) {//down left
                    if ((CheackStep(characterPositionRow + 1, characterPositionColumn)
                            && (CheackStep(characterPositionRow + 1, characterPositionColumn - 1)))
                            || (CheackStep(characterPositionRow, characterPositionColumn - 1)
                            && (CheackStep(characterPositionRow + 1, characterPositionColumn - 1)))) {
                        characterPositionRow++;
                        characterPositionColumn--;
                    }
                }
                System.out.println("characterPositionRow: "+characterPositionRow+", characterPositionColumn: "+characterPositionColumn);

            }
        }catch (Exception e){

        }
        setChanged();
        notifyObservers();
    }
    public int getCharacterPositionRow(){
        return characterPositionRow;
    }
    public int getCharacterPositionColumn(){
        return characterPositionColumn;
    }
    public int getGoalRow(){
        return goalRow;
    }
    public int getGoalColumn(){
        return goalColumn;
    }

    public void saveALL(File f){
        //URL nameFile = MyModel.class.getClassLoader().getResource("keepMaze/Maze"+numSave);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(f.getPath()));
            //MyMazePlayerData mmpd = new MyMazePlayerData();

            mt=new MyMazePlayerData(maze,goalRow,goalColumn,characterPositionColumn,characterPositionRow,mazeR,numRow,numCol);
            System.out.println("numRow:"+numRow+", numCol:"+numCol+", characterPositionRow:" + characterPositionRow+ ", characterPositionColumn:"+characterPositionColumn+ ", mazeR:" + mazeR+", maze:" + ", goalRow" +goalRow+", goalColumn"+ goalColumn);//", maze:" + ", goalRow" +goalRow+", goalColumn"+ goalColumn);
            oos.writeObject(mt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getNumRow() {
        return numRow;
    }

    public int getNumCol() {
        return numCol;
    }

    public void Load(File file){
        try{
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            MyMazePlayerData m = (MyMazePlayerData)oi.readObject();
            characterPositionColumn = m.getCharacterPositionColumn();
            characterPositionRow = m.getCharacterPositionRow();
            mazeR=m.getMazeR();
            maze=m.getMaze();
            goalColumn=m.getGoalColumn();
            goalRow=m.getGoalRow();
            this.mt = m;
            numRow=m.getNumRow();
            numCol=m.getNumCol();
            System.out.println("numRow:"+numRow+", numCol:"+numCol+", characterPositionRow:" + characterPositionRow+ ", characterPositionColumn:"+characterPositionColumn+ ", mazeR:" + mazeR+", maze:" + ", goalRow" +goalRow+", goalColumn"+ goalColumn);//", maze:" + ", goalRow" +goalRow+", goalColumn"+ goalColumn);
            setChanged();
            notifyObservers();
        }catch (IOException | ClassNotFoundException e)
        {}
    }
}
