package View;

import Model.MyModel;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public abstract class MyWindow{
    public static MazeDisplayer MazeDisp;
    public static Scene HomeScreen;
    public static Scene scene;
    public static Scene scene2;
    public static Stage primaryStage;

    public static MyViewController GamePane;

    public static int mazeRowsNumber;
    public static int mazeColumnsNumber;
    public static boolean VolumeMuted;

    public static Properties prop;

    public static void GameInstruction(Class c ){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(c.getResource("../View/GameInstructions.fxml").openStream());
            Stage stage = new Stage();
            URL inputIcon = MazeDisplayer.class.getClassLoader().getResource("Images/settingsIcon.png");
            stage.getIcons().add(new Image(inputIcon.toString()));
            stage.setTitle("Game Instuctions");
            Scene scene =  new Scene(root, 450, 450);
            scene.getStylesheets().add(c.getResource("../View/HomeScreenStyle.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void Settings(Class c ){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(c.getResource("../View/settings.fxml").openStream());
            Settings s =fxmlLoader.getController();
            s.init(false);
            Stage stage = new Stage();
            URL inputIcon = MazeDisplayer.class.getClassLoader().getResource("Images/settingsIcon.png");
            stage.getIcons().add(new Image(inputIcon.toString()));
            stage.setTitle("Game Instuctions");
            Scene scene =  new Scene(root, 500, 500);
            scene.getStylesheets().add(c.getResource("../View/HomeScreenStyle.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void About(Class c ){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(c.getResource("../View/About.fxml").openStream());
            Stage stage = new Stage();
            URL inputIcon = MazeDisplayer.class.getClassLoader().getResource("Images/settingsIcon.png");
            stage.getIcons().add(new Image(inputIcon.toString()));
            stage.setTitle("Game Instuctions");
            Scene scene =  new Scene(root, 400, 300);
            scene.getStylesheets().add(c.getResource("../View/HomeScreenStyle.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static  void exit(){
        MyModel.getInstance().ServerClose();
        Platform.exit();
        System.exit(0);
    }

}
