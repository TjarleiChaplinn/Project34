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
import java.util.concurrent.TimeUnit;

public class endController implements Initializable {

    private boolean print = false;

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

    public void switchPrint(){
        print = !print;
        System.out.println(print);
    }

    public void doTransaction() {
        try {
            App.apiConnector.makeWithdraw("1", App.keypad.pincode, (float)(App.totaalbedrag));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(print){
            App.keypad.permission = false;
            App.bon.setData(String.valueOf(App.totaalbedrag), "U heeft geld opgenomen.", "0123" /*Aanpassen naar dynamisch nummer*/, App.rfid.scannedCard.substring(0, 10) + "******");
            System.out.println("Nu printen!");
            App.bon.permission = true;
        }

        App.aantalVijf-=App.nvijf;
        App.aantalTien-=App.ntien;
        App.aantalVijftig-=App.nvijftig;

        App.totaalbedrag=0;
        App.nvijf=0;
        App.ntien=0;
        App.nvijftig=0;

        try {
            FileWriter writer = new FileWriter("./src/main/resources/data.txt");
            writer.write(String.valueOf(App.aantalVijf)+"\n");
            writer.write(String.valueOf(App.aantalTien)+"\n");
            writer.write(String.valueOf(App.aantalVijftig));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.keypad.pincode = "";
        App.rfid.scannedCard = "";

        App.keypad.permission = false;

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        App.gotoIdle();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.scene = "end";
        App.keypad.permission = true;
        bedrag2.setText("RUB: "+App.totaalbedrag);
        saldo.setText(App.balance);
        bedrag1.setText("Bon printen \u274C");
    }
}
