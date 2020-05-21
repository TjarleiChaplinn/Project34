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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.valueOf;

public class biljetController implements Initializable {

    @FXML
    Button een;
    @FXML
    Button twee;
    @FXML
    Button drie;
    @FXML
    Button een1;
    @FXML
    Button twee1;
    @FXML
    Button drie1;
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
    @FXML
    TextField saldo;

    public void vijf(){
        if(App.nvijf<App.aantalVijf){
        App.nvijf++;
        App.totaalbedrag+=5;
        vijf.setText("x "+App.nvijf);
        totaal.setText("Totaal: RUB "+App.totaalbedrag);
        }
    }
    public void tien(){
        if(App.ntien<App.aantalTien) {
            App.ntien++;
            App.totaalbedrag += 10;
            tien.setText("x " + App.ntien);
            totaal.setText("Totaal: RUB " + App.totaalbedrag);
        }
    }
    public void vijftig(){
        if(App.nvijftig<App.aantalVijftig) {
            App.nvijftig++;
            App.totaalbedrag += 50;
            vijftig.setText("x " + App.nvijftig);
            totaal.setText("Totaal: RUB " + App.totaalbedrag);
        }
    }
    public void minvijf(){
        if(App.nvijf>0) {
            App.nvijf--;
            App.totaalbedrag += -5;
            vijf.setText("x " + App.nvijf);
            totaal.setText("Totaal: RUB " + App.totaalbedrag);
        }
    }
    public void mintien(){
        if(App.ntien>0) {
            App.ntien--;
            App.totaalbedrag += -10;
            tien.setText("x " + App.ntien);
            totaal.setText("Totaal: RUB " + App.totaalbedrag);
        }
    }
    public void minvijftig(){
        if(App.nvijftig>0) {
            App.nvijftig--;
            App.totaalbedrag += -50;
            vijftig.setText("x " + App.nvijftig);
            totaal.setText("Totaal: RUB " + App.totaalbedrag);
        }
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
        if (valueOf(App.balance) >= App.totaalbedrag) {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/end.fxml"));
        Scene signupScene = new Scene(signupParent);

        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);

        window.show();
    } else {
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.setNul();
        saldo.setText(App.balance);
    }
}
