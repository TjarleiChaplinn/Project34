package Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class waitController extends Thread implements Initializable {

        @FXML
        Text info;
        @FXML
        Text message;

        public void run(){
            for(int i = 0; i < 10; i++){
                try { sleep(250); } catch (InterruptedException e) {}
                App.waitController.message.setText("Even geduld .");
                try { sleep(250); } catch (InterruptedException e) {}
                App.waitController.message.setText("Even geduld ..");
                try { sleep(250); } catch (InterruptedException e) {}
                App.waitController.message.setText("Even geduld ...");
                try { sleep(250); } catch (InterruptedException e) {}
                App.waitController.message.setText("Even geduld");
            }
            App.waitController.message.setText(
                    "Bedankt voor het wachten! " +
                    "\nWe hopen u de volgende keer weer van dienst te kunnen zijn."
            );
            try { sleep(3000); } catch (InterruptedException e) {}
            App.gotoIdle();
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            info.setText("Biljetten uitprinten:\n\u20BD5: "+App.nvijf+"\n\u20BD10: "+App.ntien+"\n\u20BD50: "+App.nvijftig+"\n\nTotaal: \u20BD"+App.totaalbedrag);
            message.setText("Even geduld");
            Thread thread = new waitController();
            thread.start();
        }
    }
