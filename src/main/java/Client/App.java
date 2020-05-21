/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Client;


import Client.lib.ApiConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class App extends Application {
    public File file = new File("./src/main/resources/data.txt");
    public Scanner scanner;
    {
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static public int totaalbedrag=0;
    static public String balance="0";
    public static ApiConnector apiConnector;
    static public int aantalVijf=0;
    static public int aantalTien=0;
    static public int aantalVijftig=0;
    static public int nvijf=0;
    static public int ntien=0;
    static public int nvijftig=0;
    static public String warning="error";

    public App() throws URISyntaxException {
    }

    //    @Override
    public void start(Stage primaryStage) throws Exception {


        // Parent root = FXMLLoader.load(getClass().getResource("/HomeOld.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/idle.fxml"));

        primaryStage.setTitle("ATM");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        aantalVijf = Integer.parseInt(scanner.nextLine());
        aantalTien = Integer.parseInt(scanner.nextLine());
        aantalVijftig = Integer.parseInt(scanner.nextLine());
        System.out.println(aantalVijf);
        System.out.println(aantalTien);
        System.out.println(aantalVijftig);
    }

    public static void main(String[] args) {
        launch();
    }
    static public void setNul(){
        totaalbedrag=0;
    }
}

