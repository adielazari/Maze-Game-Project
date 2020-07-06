package View;

import Server.Configurations;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

import java.io.*;
import java.util.Properties;

public class Settings {

    @FXML
    public javafx.scene.control.TextField rowsNum;
    public javafx.scene.control.TextField columnsNum;
    public CheckBox VolumeMute ;
    public Text textDes;
    public javafx.scene.control.ChoiceBox<String> SearcherName;
    public javafx.scene.control.ChoiceBox<String> MazeGenerator;

    private String generator;
    private String Searcher;

    private Properties prop;
    public void init(boolean firstRun){
        try {
            if(firstRun) {
                InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties");
                prop = new Properties();
                prop.load(in);
            }else{
                prop = MyWindow.prop;
            }


            Searcher = prop.getProperty("searchName");
            System.out.println(Searcher);
            generator=prop.getProperty("MazeGenerator");
            System.out.println(generator);
            SearcherName.setValue(Searcher);
            MazeGenerator.setValue(generator);
            String volume = prop.getProperty("Volume");
            if(volume.equals("false")){
                MyWindow.VolumeMuted=true;
                VolumeMute.setSelected(true);
            }else{
                MyWindow.VolumeMuted=false;
                VolumeMute.setSelected(false);
            }
            MyWindow.prop = prop;
            try {
                int rows = Integer.parseUnsignedInt(prop.getProperty("rowsNumber")) ;
                int columns = Integer.parseUnsignedInt(prop.getProperty("columnsNumber")) ;
                if( rows < 3 || columns<3){
                    MyWindow.mazeRowsNumber=10;
                    MyWindow.mazeColumnsNumber=10;
                }else{
                    MyWindow.mazeRowsNumber=rows;
                    MyWindow.mazeColumnsNumber=columns;
                }
                rowsNum.setText(prop.getProperty("rowsNumber"));
                columnsNum.setText(prop.getProperty("columnsNumber"));
            }catch (Exception e){
                MyWindow.mazeRowsNumber=10;
                MyWindow.mazeColumnsNumber=10;
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        MyWindow.prop = prop;

    }
    public void ApplyChanges(){
        try/*InputStream in=  new FileInputStream("./resources/config.properties") /*getClass().getClassLoader().getResourceAsStream("config.properties")*/ {
          //  Properties prop = new Properties();
          //  prop.load(in);
            int rows = 0;
            int columns = 0;
            try {
                rows = Integer.valueOf(rowsNum.getText());
                columns = Integer.valueOf(columnsNum.getText());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Error!");
            }
            if (rows < 5 || columns < 5) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Error! you must to insert numbers greater than 5");
                //    alert.setTitle("back to Home screen");
                alert.getButtonTypes().remove(0);
                //  Button button = new Button("Countinue");
                alert.getButtonTypes().add(ButtonType.OK);
                alert.show();
                return;
            } else {
                prop.setProperty("rowsNumber", String.valueOf(rows));
                prop.setProperty("columnsNumber", String.valueOf(columns));
                MyWindow.mazeRowsNumber = rows;
                MyWindow.mazeColumnsNumber = columns;
                textDes.setText("Changed Successfully!");
            }
            SaveProp(prop);
        }catch (Exception e){

        }
    }
    public void ApplySearchers(){
        try{
            prop.setProperty("searchName", SearcherName.getValue());
            prop.setProperty("MazeGenerator", MazeGenerator.getValue());
            textDes.setText("Changed Successfully!");
            SaveProp(prop);
        }catch (Exception e){

        }
    }
    public void  VolumeMute(){
        try{
            MyWindow.VolumeMuted = VolumeMute.isSelected();
            System.out.println("VolumeMute : " + VolumeMute.isSelected());
            if (VolumeMute.isSelected()) {
                textDes.setText("volume turned off");
                prop.setProperty("Volume" ,"false");
            } else {
                textDes.setText("volume turned on");
                prop.setProperty("Volume" ,"true");
            }
            SaveProp(prop);
        }catch (Exception e){

        }

    }
    private void SaveProp(Properties prop){
        try{
            OutputStream output =  new FileOutputStream("./resources/config.properties");
            prop.store(output, "");
            Configurations.setProp(prop);
            output.flush();
            output.close();
        }catch (Exception e){

        }
    }
}
