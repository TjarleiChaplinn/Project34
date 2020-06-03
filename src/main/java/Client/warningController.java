package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class warningController implements Initializable {

    @FXML
    Button menu;
    @FXML
    TextField saldo;
    @FXML
    Button stop;
    @FXML
    Text warningtext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.scene = "warning";
        App.keypad.permission = true;
        saldo.setText(App.balance);
        warningtext.setText(App.warning);
    }
}
