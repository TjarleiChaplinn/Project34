package Client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class idleController extends Thread implements Initializable {

    @FXML
    Button start;

    public void start(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene signupScene = new Scene(signupParent);

        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }

    public void run(){
        boolean temp = true;
        while(temp){
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(App.rfid.scannedCard != "") {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Parent signupParent = FXMLLoader.load(getClass().getResource("/login.fxml"));
                            Scene signupScene = new Scene(signupParent);
                            App.primaryStage.setScene(signupScene);
                            App.primaryStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                temp = false;
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.rfid.permission = true;
        Thread thread = new idleController();
        thread.setName("Idle Thread");
        thread.start();
    }
}
