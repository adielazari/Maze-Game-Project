package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeScreenController {
    public void enterMazeSize(){
        MyWindow.primaryStage.setScene(MyWindow.scene);
        MyWindow.primaryStage.show();
    }
    public void newGame(){
        enterMazeSize();
    }

    public void GameInstruction(){
        MyWindow.GameInstruction(getClass());
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
    public void loadGame(){
        MyWindow.GamePane.loadGame();
    }

}
