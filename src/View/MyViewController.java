package View;


import Model.MyMazePlayerData;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import jdk.internal.util.xml.impl.Input;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.Key;
import java.util.Observable;
import java.util.Observer;


public class MyViewController implements Observer, IView{

    @FXML
    public MazeDisplayer MazeDISPLAY;
    public javafx.scene.control.TextField rowsNum;
    public javafx.scene.control.TextField columnsNum;
    public int row;
    public int col;

    private MyViewModel ViewModel;

    public void setViewModel(MyViewModel viewModel) {
        this.ViewModel = viewModel;
    }
    @Override
    public void update(Observable o, Object arg) {
        if (o == ViewModel) {
            if(MyWindow.MazeDisp != null)
             displayMaze(ViewModel.getRows(),ViewModel.getCOl(),ViewModel.getMaze() , ViewModel.getGoalRow() , ViewModel.getGoalColumn());
        }

    }

 //   @Override
    public void displayMaze(int rows,int col, int[][] maze , int goalRow , int goalColumn) {
        MyWindow.MazeDisp.setMaze(maze , goalRow ,goalColumn);
        int characterPositionRow = ViewModel.getCharacterPositionRow();
        int characterPositionColumn = ViewModel.getCharacterPositionColumn();
        MyWindow.MazeDisp.setCharacterPosition(characterPositionRow, characterPositionColumn);
        row=rows;
        this.col=col;
    }

    private  MediaPlayer mediaPlayer ;

    public void CreateMazeWithDefaultSizes(){
        playMaze(MyWindow.mazeRowsNumber ,MyWindow.mazeColumnsNumber,true);
    }
    public void CreateMaze (){
        int rows = 0;
        int columns = 0;
        try {
            rows = Integer.valueOf(rowsNum.getText());
            columns = Integer.valueOf(columnsNum.getText());
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Error!");
        }
        if(rows < 5 || columns<5){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Error! you must to insert numbers greater than 5");
        //    alert.setTitle("back to Home screen");
            alert.getButtonTypes().remove(0);
            //  Button button = new Button("Countinue");
            alert.getButtonTypes().add( ButtonType.OK);
            alert.show();
            MyWindow.primaryStage.setScene(MyWindow.scene);
            MyWindow.primaryStage.show();
            return;
        }
        playMaze(rows,columns,true);
    }

    public void playMaze(int rows, int columns,Boolean newGame){

        try {
            MyWindow.primaryStage.setResizable(true);
        //  ViewModel = new MyViewModel();
        //   int rows = Integer.valueOf(rowsNum.getText());
        //  int columns = Integer.valueOf(columnsNum.getText());

        Button solution_Button = new Button("solution");
        Button back_button = new Button("Back");
        Button save = new Button("Save");
        Label saveL = new Label();
        ToolBar tb = new ToolBar();
        tb.getItems().addAll(back_button, save,solution_Button);

        MyWindow.MazeDisp = new MazeDisplayer(400,400);//rows*25, columns*25



        ScrollPane sp = new ScrollPane();
        sp.setContent(  MyWindow.MazeDisp);

        VBox vb = new VBox(tb, sp);



        MyWindow.scene2 = new Scene(vb ,500, 600 );
            vb.heightProperty().addListener((observable, oldValue, newValue) -> {
                MyWindow.MazeDisp.heightProperty().set((double) newValue - 45);
            });
            vb.widthProperty().addListener((observable, oldValue, newValue) -> {
                MyWindow.MazeDisp.widthProperty().set((double) newValue -5 );
            });

        MazeDISPLAY = MyWindow.MazeDisp;



        MazeDISPLAY.displaySettings(rows, columns);

        back_button.setOnAction(e -> BackToSizeSelection());
        save.setOnAction(e->saveGame());
        solution_Button.setOnAction(e -> solveIt());

            MyWindow.scene2.setOnScroll(new EventHandler<ScrollEvent>() {
                @Override
                public void handle(ScrollEvent event) {
                    if(event.isControlDown()) {
                        double zoomFactor = 1.05;
                        if (event.getDeltaY() <= 0) {
                            // zoom out
                            zoomFactor = 1 / zoomFactor;
                        }

                        MyWindow.MazeDisp.ReSizeMze(zoomFactor);
                    }
                }
            });

         if( ! MyWindow.VolumeMuted) {
             URL input = MazeDisplayer.class.getClassLoader().getResource("Ringtons/LightlessDawn.mp3");
             String ssound = input.toString();
             Media sound = new Media(ssound);
             mediaPlayer = new MediaPlayer(sound);
             mediaPlayer.play();
         }
        System.out.println(rows + "," + columns);
        Thread.sleep(500);

        MyWindow.scene2.setOnKeyPressed(event -> {
            KeyPressed(event);
        });


        MyWindow.scene2.getStylesheets().add(getClass().getResource("../View/ViewStyle.css").toExternalForm());
        MyWindow.primaryStage.setScene(MyWindow.scene2);
        MyWindow.primaryStage.show();
        ViewModel.generateMaze(rows, columns,newGame);


    }catch (Exception e){

    }
}
    public void setStage(){

    }
    public void BackToHomeScreen(){
        MyWindow.primaryStage.setScene(MyWindow.HomeScreen);
        MyWindow.primaryStage.show();
    }
    public void BackToSizeSelection(){
        if (! MyWindow.VolumeMuted) {
            mediaPlayer.stop();
        }
        MyWindow.primaryStage.setScene(MyWindow.scene);
        MyWindow.primaryStage.show();
    }
    public void goToMazeScreen(){
        MyWindow.primaryStage.setScene(MyWindow.scene2);
        MyWindow.primaryStage.show();
    }
    public void solveIt(){
        MazeDISPLAY.printSolution( ViewModel.getSolution());

    }
    public void GameInstruction(){
        MyWindow.GameInstruction(getClass());
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Woooooooooooooooow!!!!!!!!!!!!");
        alert.setTitle("back to Home screen");
        alert.getButtonTypes().remove(0);
      //  Button button = new Button("Countinue");
        alert.getButtonTypes().add( ButtonType.FINISH);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void KeyPressed(KeyEvent keyEvent) {
        ViewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
        if(ViewModel.getGoalRow() == ViewModel.getCharacterPositionRow() && ViewModel.getGoalColumn()==ViewModel.getCharacterPositionColumn()){
            if( ! MyWindow.VolumeMuted) {
                mediaPlayer.stop();
                URL input = MazeDisplayer.class.getClassLoader().getResource("Ringtons/mazeSolved.mp3");
                String ssound = input.toString();
                Media sound = new Media(ssound);
                MediaPlayer mp = new MediaPlayer(sound);
                mp.play();
            }
              showAlert("Well done! you did it !!!!!!!!");
            BackToSizeSelection();
        }
    }

    public void newGame() {
        rowsNum.setText("");
        columnsNum.setText("");
        BackToSizeSelection();
    }

    public void saveGame() {
        final FileChooser fc = new FileChooser();
        File f = fc.showSaveDialog(null);
        if (f!=null) {
            // user selects a file
            File selectedFile = new File(f.getPath());
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            ViewModel.saveGame(selectedFile);
        }
    }

    public void loadGame() {
        CreateMazeWithDefaultSizes();
        BackToSizeSelection();
        final FileChooser fc = new FileChooser();
        File f =fc.showOpenDialog(null);
        if (f!=null) {
            // user selects a file
            File selectedFile = new File(f.getPath());
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());

            ViewModel.loadGame(selectedFile);
        }
        //System.out.println("numRow:"+row+", numCol:"+col+", characterPositionRow:" + characterPositionRow+ ", characterPositionColumn:"+characterPositionColumn+ ", mazeR:" + mazeR+", maze:" + ", goalRow" +goalRow+", goalColumn"+ goalColumn);//", maze:" + ", goalRow" +goalRow+", goalColumn"+ goalColumn);

        playMaze(row,col,false);

    }
    public void Properties(){
        MyWindow.Settings(this.getClass());

    }
    public void About(){
        MyWindow.About(this.getClass());
    }
    public void Exit(){
        MyWindow.exit();
    }
}
