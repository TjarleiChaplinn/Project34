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

public class bullshit {

    @FXML
    Button knop1;
    @FXML
    Button knop2;
    @FXML
    TextField textfield;

    public void nextpage(ActionEvent event)throws IOException {
    String text = textfield.getText();
    if(text.equals("een")){
        Parent signupParent = FXMLLoader.load(getClass().getResource("/wachtwoordScherm.fxml"));
        Scene signupScene = new Scene(signupParent);

        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }
    }
    public void exit(ActionEvent event)throws IOException{
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }
}
