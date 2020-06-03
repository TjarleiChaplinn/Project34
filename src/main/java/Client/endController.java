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

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class endController implements Initializable {
    @FXML
    Button end;
    @FXML
    Button menu;
    @FXML
    Button stop;
    @FXML
    Button bon;
    @FXML
    Text bedrag2;
    @FXML
    Text bedrag1;
    @FXML
    TextField saldo;

    boolean bonPrinten=false;

    public void menu(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Scene signupScene = new Scene(signupParent);

        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();

    }
    public void stop(ActionEvent event) throws IOException {
        Parent signupParent = FXMLLoader.load(getClass().getResource("/idle.fxml"));
        Scene signupScene = new Scene(signupParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }
    public void bon(ActionEvent event) throws IOException {
        if(bonPrinten==false) {
            bonPrinten=true;
            bedrag1.setText("Bon printen \u2713");
        }else{
            bonPrinten=false;
            bedrag1.setText("Bon printen \u274C");
        }
    }
    public void pin(ActionEvent event) throws IOException {
        App.apiConnector.makeWithdraw("1", "8459", (float)(App.totaalbedrag));
        FileWriter writer = new FileWriter("./src/main/resources/data.txt");
        App.aantalVijf+=-App.nvijf;
        App.aantalTien+=-App.ntien;
        App.aantalVijftig+=-App.nvijftig;
        writer.write(String.valueOf(App.aantalVijf)+"\n");
        writer.write(String.valueOf(App.aantalTien)+"\n");
        writer.write(String.valueOf(App.aantalVijftig));
        writer.close();
        if(bonPrinten){
            //App.bon.permission = true;
        }


        Parent signupParent = FXMLLoader.load(getClass().getResource("/idle.fxml"));
        Scene signupScene = new Scene(signupParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signupScene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bedrag2.setText("RUB: "+App.totaalbedrag);
        saldo.setText(App.balance);
        bedrag1.setText("Bon printen \u274C");
    }
}
