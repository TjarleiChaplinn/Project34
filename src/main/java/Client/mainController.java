package Client;

import javafx.application.Platform;
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

    public void gotoBiljetScene() {
        if (valueOf(App.balance) >= 70) {
            int balanceTemp = App.aantalVijf * 5 + App.aantalTien * 10 + App.aantalVijftig * 50;
            if(balanceTemp > 0){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/biljet.fxml"));
                Parent signupParent = null;
                try {
                    signupParent = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                App.biljetController = (biljetController) loader.getController();

                Scene signupScene = new Scene(signupParent);
                App.primaryStage.setScene(signupScene);
                App.primaryStage.show();
            }
            else{
                App.gotoWarningScene("Te weinig geld in automaat");
            }
        }
        else {
            App.gotoWarningScene("Te weinig saldos");
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
        App.scene = "main";
        App.keypad.permission = true;
        saldo.setText(App.balance);
    }
}
