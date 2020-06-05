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

import static java.lang.Float.valueOf;

public class mainController implements Initializable {

    @FXML
    Button pin;
    @FXML
    Button snel;
    @FXML
    Button stop;
    @FXML
    Button saldoScherm;

    public void gotoSaldoScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/saldo.fxml"));
        Parent signupParent = null;
        try {
            signupParent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.saldoController = (saldoController) loader.getController();

        Scene signupScene = new Scene(signupParent);
        App.primaryStage.setScene(signupScene);
        App.primaryStage.show();
    }

    public void gotoBiljetScene() {
        int balanceTemp = App.aantalVijf * 5 + App.aantalTien * 10 + App.aantalVijftig * 50;
        if (valueOf(App.balance) >= 5) {
            if(balanceTemp >= 5){
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
            } else {
                App.gotoWarningScene("Te weinig geld in automaat");
            }
        } else {
            App.gotoWarningScene("Te weinig saldo");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.scene = "main";
        App.keypad.permission = true;
    }
}
