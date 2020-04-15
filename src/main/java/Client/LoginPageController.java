package Client;

import Client.lib.ApiConnector;
import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.awt.*;
import java.io.IOException;

public class LoginPageController {

    private int counter = 0;

    @FXML
    PasswordField pinCodeField;

    @FXML
    public void logIn(ActionEvent event) throws IOException {
        String password =pinCodeField.getText();

        //try to login to server
        try {
            App.apiConnector = new ApiConnector("test", password, false);
            System.out.println(App.apiConnector.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("API connection error: " + e.getMessage());
        }

        if (counter == 3) {
            passBlockedNotification();
            return;
        }

        if (password.equals("1234")) {
            loginSuccessNotification();
            counter = 0;

            Parent signupParent = FXMLLoader.load(getClass().getResource("/Home.fxml"));
            Scene signupScene = new Scene(signupParent);

            //This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(signupScene);
            window.show();
        }
        else {
            counter++;
            loginFailedNotification();
        }


        if (counter == 3) {
            passBlockedNotification();
        }
    }

    /**
     * This method controls the notification that pops up when you successfully login.
     */

    @FXML
    public void loginSuccessNotification() {

        Notifications notificationbuilder = Notifications.create()
                .title("Successful")
                .text("You are logged in")
                .graphic(null)
                .position(Pos.BOTTOM_RIGHT);

        notificationbuilder.showConfirm();
    }

    /**
     * This method controls the notification that pops up when you fail to login.
     */

    @FXML
    public void loginFailedNotification() {

        Notifications notificationbuilder = Notifications.create()
                .title("Failed")
                .text("Incorrect pin code, " + (3-counter) + " attempts remaining")
                .graphic(null)
                .position(Pos.BOTTOM_RIGHT);

        notificationbuilder.showConfirm();
    }

    @FXML
    public void passBlockedNotification() {

        Notifications notificationbuilder = Notifications.create()
                .title("Blocked")
                .text("Your debit card has been blocked because you entered the wrong pin code 3 times")
                .graphic(null)
                .position(Pos.BOTTOM_RIGHT);

        notificationbuilder.showConfirm();
    }
}
