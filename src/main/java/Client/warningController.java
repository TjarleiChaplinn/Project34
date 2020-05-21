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

    public void stop(ActionEvent event) throws IOException {
        App.setNul();
        Parent signupParent = FXMLLoader.load(getClass().getResource("/idle.fxml"));
        Scene signupScene = new Scene(signupParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }
    public void menu(ActionEvent event) throws IOException {
        App.setNul();
        Parent signupParent = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Scene signupScene = new Scene(signupParent);

        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saldo.setText(App.balance);
        warningtext.setText(App.warning);
    }
}
