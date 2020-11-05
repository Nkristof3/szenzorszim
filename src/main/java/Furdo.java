import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


//MOZGAST NEM ERZEKEL
public class Furdo extends Application {

    String txtName = "Furdo"; //Módosít
    TextField txtInput;
    public GridPane gridPane;
    public ScrollPane scrollPane;
    public TextArea txtAreaDisplay;

    // IO streams
    DataOutputStream output = null;
    Socket socket;

    @Override
    public void stop() throws Exception {
        socket.close();
    }

    public void createClient(String nev, String kep_neve, Stage ablak){
        //pane to hold scroll pane and HBox
        txtAreaDisplay = new TextArea();
        VBox vBox = new VBox(5);
        gridPane = new GridPane();
        scrollPane = new ScrollPane();
        vBox.setPrefSize(300, 350);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-background-image: url('"+kep_neve+"');" +
                "-fx-background-repeat: stretch;" +
                "-fx-background-size: 350 300;" +
                "-fx-background-position: center center;");

        scrollPane.setContent(vBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        gridPane.add(scrollPane, 1, 1);
        gridPane.getRowConstraints().add(new RowConstraints(30));
        gridPane.add(txtAreaDisplay, 1, 2);
        GridPane.setMargin(scrollPane, new Insets(10, 10, 10, 10));
        GridPane.setMargin(txtAreaDisplay, new Insets(10, 10, 10, 10));

        vBox.hoverProperty().addListener((ChangeListener<Boolean>) (observable, value, newValue) -> {
            if (newValue) {
                try {
                    output.writeUTF("be");
                    /*scrollPane.setStyle("-fx-border-color: yellow;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 1.0");*/

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    output.writeUTF("ki");

                    scrollPane.setStyle("-fx-border-color: black;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 0.5");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        });


        //create a scene and display
        Scene scene = new Scene(gridPane, 450, 500);
        ablak.setTitle(nev + " szenzor");
        ablak.setScene(scene);
        ablak.show();
    }

    @Override
    public void start(Stage primaryStage) {
        //pane to hold scroll pane and HBox
        txtAreaDisplay = new TextArea();
        VBox vBox = new VBox(5);
        gridPane = new GridPane();
        scrollPane = new ScrollPane();
        vBox.setPrefSize(300, 350);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-background-image: url('furdoszoba.jpg');" + //
                "-fx-background-repeat: stretch;" +
                "-fx-background-size: 400 350;" +
                "-fx-background-position: center center;");

        scrollPane.setContent(vBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        gridPane.add(scrollPane, 1, 1);
        gridPane.getRowConstraints().add(new RowConstraints(30));
        gridPane.add(txtAreaDisplay, 1, 2);
        GridPane.setMargin(scrollPane, new Insets(10, 10, 10, 10));
        GridPane.setMargin(txtAreaDisplay, new Insets(10, 10, 10, 10));

        vBox.hoverProperty().addListener((ChangeListener<Boolean>) (observable, value, newValue) -> {
            if (newValue) {
                try {
                    output.writeUTF("be");
                    /*scrollPane.setStyle("-fx-border-color: yellow;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 1.0");*/

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    output.writeUTF("ki");

                    scrollPane.setStyle("-fx-border-color: black;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 0.5");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        });


        //create a scene and display
        Scene scene = new Scene(gridPane, 450, 500);
        primaryStage.setTitle(txtName + " szenzor");
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image("furdoikon.jpg")); //ikon
        primaryStage.setScene(scene);
        primaryStage.show();

        try {
            // Create a socket to connect to the server
            socket = new Socket(Connect.host, Connect.port);

            //Connection successful
            txtAreaDisplay.appendText("Csatlakozva. \n");

            // Create an output stream to send data to the server
            output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(txtName);
            //create a thread in order to read message from server continuously
            TaskRead task = new TaskRead(socket, this);
            Thread thread = new Thread(task);
            thread.start();
        } catch (IOException ex) {

            txtAreaDisplay.appendText(ex.toString() + '\n');
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}