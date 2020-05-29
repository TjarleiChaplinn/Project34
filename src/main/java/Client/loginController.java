package Client;

import Client.lib.ApiConnector;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class loginController extends Thread implements Initializable {

    public static boolean runThread = true;
    private int counter=0;

    @FXML
    PasswordField pin;
    @FXML
    Button stop;
    @FXML
    Button ok;
    @FXML
    Text melding;
    @FXML
    Text bedrag1;

    public void menu(ActionEvent event) throws IOException {
        String password = App.keypad.pincode;
        try {
            App.apiConnector = new ApiConnector("1", password, false);
            if (App.apiConnector.verifyPin("1", password)==true){
                App.balance=App.apiConnector.getBalance("1", password);
                Parent signupParent = FXMLLoader.load(getClass().getResource("/main.fxml"));
                Scene signupScene = new Scene(signupParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                window.setScene(signupScene);
                window.show();
            } else{
                melding.setText(App.apiConnector.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("API connection error: " + e.getMessage());
        }
        App.keypad.pincode = "";
    }

    public void stop(ActionEvent event) throws IOException {
    }

    public static void addKey(){
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_0);
                    robot.keyRelease(KeyEvent.VK_0);

                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void removeKey(){
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_BACK_SPACE);
                    robot.keyRelease(KeyEvent.VK_BACK_SPACE);

                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize LoginController");
        System.out.println("Permission : " + App.connection.permission);
        App.keypad.permission = true;
        pin.requestFocus();
    }
}