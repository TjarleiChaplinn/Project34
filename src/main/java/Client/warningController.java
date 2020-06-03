package Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class warningController implements Initializable {

    @FXML
    Text warningtext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.scene = "warning";
        App.keypad.permission = true;
        warningtext.setText(App.warning);
    }
}
