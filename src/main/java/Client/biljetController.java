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

    public void addFive() {
        if (App.nvijf < App.aantalVijf) {
            App.nvijf++;
            App.totaalbedrag += 5;
            vijf.setText("x " + App.nvijf);
            totaal.setText("Totaal: \u20BD" + App.totaalbedrag);
        }
    }

    public void addTen() {
        if (App.ntien < App.aantalTien) {
            App.ntien++;
            App.totaalbedrag += 10;
            tien.setText("x " + App.ntien);
            totaal.setText("Totaal: \u20BD" + App.totaalbedrag);
        }
    }

    public void addFifty() {
        if (App.nvijftig < App.aantalVijftig) {
            App.nvijftig++;
            App.totaalbedrag += 50;
            vijftig.setText("x " + App.nvijftig);
            totaal.setText("Totaal: \u20BD" + App.totaalbedrag);
        }
    }

    public void remFive() {
        if (App.nvijf > 0) {
            App.nvijf--;
            App.totaalbedrag += -5;
            vijf.setText("x " + App.nvijf);
            totaal.setText("Totaal: \u20BD" + App.totaalbedrag);
        }
    }

    public void remTen() {
        if (App.ntien > 0) {
            App.ntien--;
            App.totaalbedrag += -10;
            tien.setText("x " + App.ntien);
            totaal.setText("Totaal: \u20BD" + App.totaalbedrag);
        }
    }

    public void remFifty() {
        if (App.nvijftig > 0) {
            App.nvijftig--;
            App.totaalbedrag += -50;
            vijftig.setText("x " + App.nvijftig);
            totaal.setText("Totaal: \u20BD" + App.totaalbedrag);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.scene = "biljet";
        App.keypad.permission = true;
        App.nvijf=0;
        App.ntien=0;
        App.nvijftig=0;
        vijf.setText("x " + App.nvijf);
        tien.setText("x " + App.ntien);
        vijftig.setText("x " + App.nvijftig);
        App.setNul();
    }
}
