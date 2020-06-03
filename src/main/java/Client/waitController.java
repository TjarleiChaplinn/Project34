package Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

    public class waitController implements Initializable {

        @FXML
        Text info;

        public void next(ActionEvent event) throws IOException {
            Parent signupParent = FXMLLoader.load(getClass().getResource("/idle.fxml"));
            Scene signupScene = new Scene(signupParent);

            //This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(signupScene);
            window.show();
        }
        @Override
        public void initialize(URL location, ResourceBundle resources) {
            info.setText("Biljetten uitprinten:\n€5: "+App.nvijf+"\n€10: "+App.ntien+"\n€50: "+App.nvijftig+"\n\nTotaal: €"+App.totaalbedrag );
        }
    }
