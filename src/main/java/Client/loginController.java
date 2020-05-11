package Client;

import Client.lib.ApiConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class loginController {
    private int counter=0;
    @FXML
    PasswordField pin;
    @FXML
    Button stop;
    @FXML
    Button ok;
    @FXML
    Text melding;

    public void menu(ActionEvent event) throws IOException {
        String password = pin.getText();
        try {
            App.apiConnector = new ApiConnector("test", password, false);
            if (App.apiConnector.verifyPin("1", password)==true){
                App.balance=App.apiConnector.getBalance("1", password);
                Parent signupParent = FXMLLoader.load(getClass().getResource("/main.fxml"));
                Scene signupScene = new Scene(signupParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                window.setScene(signupScene);
                window.show();
            } else{
                melding.setText(App.apiConnector.getMessage());
            }
//            System.out.println(App.apiConnector.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("API connection error: " + e.getMessage());
        }


//        if (counter == 3) {
//            melding.setText("Pas geblokkeerd!");
//        } else if(password=="1234"){
//            int counter=0;
//            Parent signupParent = FXMLLoader.load(getClass().getResource("/menu.fxml"));
//            Scene signupScene = new Scene(signupParent);
//            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
//
//            window.setScene(signupScene);
//            window.show();
////        }else if(password!="1234"){
////            counter++;
////            melding.setText("Foute pincode, "+(3-counter)+" pogingen over "+password);
//       }

    }

    public void stop(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/idle.fxml"));
        Scene signupScene = new Scene(signupParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }
}
