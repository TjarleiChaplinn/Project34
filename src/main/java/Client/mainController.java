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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.valueOf;

public class mainController implements Initializable {

    @FXML
    Button pin;
    @FXML
    Button snel;
    @FXML
    TextField saldo;
    @FXML
    Button stop;
    @FXML
    Button saldoscherm;

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
        if (valueOf(App.balance) >= 70) {
            if(App.aantalVijftig>=1||App.aantalTien>=2){
            App.setNul();
            App.totaalbedrag = 70;
            App.nvijftig=1;
            App.ntien=2;
            Parent signupParent = FXMLLoader.load(getClass().getResource("/end.fxml"));
            Scene signupScene = new Scene(signupParent);

            //This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(signupScene);

            window.show();
        }else{
                App.warning="Te weinig geld in automaat";
                Parent signupParent = FXMLLoader.load(getClass().getResource("/warning.fxml"));
                Scene signupScene = new Scene(signupParent);

                //This line gets the Stage information
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                window.setScene(signupScene);

                window.show();
            }
        } else {
            App.warning="Te weinig balans";
            Parent signupParent = FXMLLoader.load(getClass().getResource("/warning.fxml"));
            Scene signupScene = new Scene(signupParent);

            //This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(signupScene);

            window.show();
        }
    }

    public void stop(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/idle.fxml"));
        Scene signupScene = new Scene(signupParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }
    public void saldo(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/saldo.fxml"));
        Scene signupScene = new Scene(signupParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    saldo.setText(App.balance);
    }
}
