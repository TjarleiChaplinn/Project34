/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Client;


import Client.lib.ApiConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    static public int totaalbedrag=0;
    static public String balance="0";
    public static ApiConnector apiConnector;

//    @Override
    public void start(Stage primaryStage) throws Exception {


        // Parent root = FXMLLoader.load(getClass().getResource("/HomeOld.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/idle.fxml"));

        primaryStage.setTitle("ATM");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    static public void setNul(){
        totaalbedrag=0;
    }
}
