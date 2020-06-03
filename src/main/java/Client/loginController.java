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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class loginController implements Initializable {
    private int counter=0;

    @FXML
    PasswordField pin;
    @FXML
    Button stop;
    @FXML
    Button ok;
    @FXML
    public Text melding;
    @FXML
    Text bedrag1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize LoginController");
        App.scene = "login";
        App.keypad.permission = true;
        pin.requestFocus();
        App.keypad.pin = true;

    }

    public void setPinText() {
        String pinTextTemp = "";
        for(int i = 0; i < App.keypad.pincode.length(); i++){
            pinTextTemp += '*';
        }
        pin.setText(pinTextTemp);
    }
}
