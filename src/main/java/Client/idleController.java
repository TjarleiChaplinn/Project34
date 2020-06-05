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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class idleController extends Thread implements Initializable {

    public void run(){
        boolean temp = true;
        while(temp){
            App.rfid.scannedCard = "";
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
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
                            Parent signupParent = loader.load();

                            App.loginController = (loginController)loader.getController();

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
        App.scene = "idle";
        Thread thread = new idleController();
        thread.setName("Idle Thread");
        thread.start();
        App.totaalbedrag=0;
        App.nvijf=0;
        App.ntien=0;
        App.nvijftig=0;
    }
}
