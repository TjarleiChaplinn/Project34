package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class biljetController implements Initializable {

    public int nvijf=0;
    public int ntien=0;
    public int nvijftig=0;

    @FXML
    Button een;
    @FXML
    Button twee;
    @FXML
    Button drie;
    @FXML
    Button stop;
    @FXML
    Button menu;
    @FXML
    Button pin;
    @FXML
    Text vijf;
    @FXML
    Text tien;
    @FXML
    Text vijftig;
    @FXML
    Text totaal;

    public void vijf(){
        nvijf++;
        App.totaalbedrag+=5;
        vijf.setText("x "+nvijf);
        totaal.setText("Totaal: RUB "+App.totaalbedrag);
    }
    public void tien(){
        ntien++;
        App.totaalbedrag+=10;
        tien.setText("x "+ntien);
        totaal.setText("Totaal: RUB "+App.totaalbedrag);
    }
    public void vijftig(){
        nvijftig++;
        App.totaalbedrag+=50;
        vijftig.setText("x "+nvijftig);
        totaal.setText("Totaal: RUB "+App.totaalbedrag);
    }

    public void menu(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Scene signupScene = new Scene(signupParent);

        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }
    public void pin(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/end.fxml"));
        Scene signupScene = new Scene(signupParent);

        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
//        endController obj = new endController();
//        window.setOnShowing(e-> obj.execute());
        window.show();
    }
    public void stop(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/idle.fxml"));
        Scene signupScene = new Scene(signupParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
