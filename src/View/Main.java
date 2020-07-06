package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.text.View;
import java.awt.*;

import java.beans.EventHandler;
import java.io.File;
import java.net.URL;

import static View.MazeDisplayer.backgroundCode;

public class Main extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception{

        MyModel model = MyModel.getInstance();

        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);

        //-----------------------------
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("../View/MyView.fxml").openStream());
        MyWindow.scene = new Scene(root,300,170);
        MyWindow.scene.getStylesheets().add(getClass().getResource("../View/ViewStyle.css").toExternalForm());

        FXMLLoader fxmlLoader2 = new FXMLLoader();
        Parent home = fxmlLoader2.load(getClass().getResource("../View/HomeScreen.fxml").openStream());
        MyWindow.HomeScreen = new Scene(home,500,400);
        MyWindow.HomeScreen.getStylesheets().add(getClass().getResource("../View/HomeScreenStyle.css").toExternalForm());


        primaryStage.setScene(MyWindow.HomeScreen);
        primaryStage.setResizable(false);

        MyWindow.primaryStage = primaryStage;

        FXMLLoader fxmlLoader5 = new FXMLLoader();
        fxmlLoader5.load(getClass().getResource("../View/settings.fxml").openStream());
        Settings s =fxmlLoader5.getController();
        s.init(true);

        URL inputIcon = MazeDisplayer.class.getClassLoader().getResource("Images/tomANDjerry.png");
        MyWindow.primaryStage.getIcons().add(new Image(inputIcon.toString()));
        MyWindow.primaryStage.setTitle("Tom & Jerry");
      //  ---------------------
        MyViewController view = fxmlLoader.getController();
        MyWindow.GamePane = view;
      //  view.setResizeEvent(scene);
        view.setViewModel(viewModel);
        viewModel.addObserver(view);

//-------------------------------------------
        primaryStage.show();


        primaryStage.setOnCloseRequest(e ->  {
               MyWindow.exit();
        });

    }




    public static void main(String[] args) {
        launch(args);
    }
}
