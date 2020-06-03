package Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class saldoController implements Initializable {

    @FXML
    Button stop;
    @FXML
    Button menu;
    @FXML
    TextField saldo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.scene = "saldo";
        App.keypad.permission = true;
        saldo.setText(App.balance);
    }
}
