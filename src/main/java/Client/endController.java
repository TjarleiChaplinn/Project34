package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class endController {
    @FXML
    Button end;
    @FXML
    Button menu;
    @FXML
    Button stop;
    @FXML
    Text bedrag;
    public void execute(){
        bedrag.setText("RUB: "+App.totaalbedrag);
    }

    public void menu(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Scene signupScene = new Scene(signupParent);

        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();

    }
    public void stop(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/idle.fxml"));
        Scene signupScene = new Scene(signupParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }
}
