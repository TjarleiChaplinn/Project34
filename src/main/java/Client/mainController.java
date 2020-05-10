package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class mainController {

    @FXML
    Button pin;
    @FXML
    Button snel;
    @FXML
    TextField saldo;
    @FXML
    Button exit;

    public void pinPage(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/biljet.fxml"));
        Scene signupScene = new Scene(signupParent);

        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        App.setNul();
        window.show();

    }
    public void snelPin(ActionEvent event) throws IOException {
        App.setNul();
        App.totaalbedrag+=70;
        Parent signupParent = FXMLLoader.load(getClass().getResource("/end.fxml"));
        Scene signupScene = new Scene(signupParent);

        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);

        window.show();
//        endController obj = new endController();

//        obj.bedrag2.setText("asdasd");
    }

    public void stop(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/idle.fxml"));
        Scene signupScene = new Scene(signupParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }
}