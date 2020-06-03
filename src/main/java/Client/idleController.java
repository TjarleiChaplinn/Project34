package Client;

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

public class idleController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.totaalbedrag=0;
        App.nvijf=0;
        App.ntien=0;
        App.nvijftig=0;
    }
}
