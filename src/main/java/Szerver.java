import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Szerver extends Application {
    /*@FXML
    TextArea txtAreaDisplay;*/
    public TextArea txtAreaDisplay;
    public CheckBox konyhaBox;
    public CheckBox nappaliBox;
    List<KliensConn> connectionList = new ArrayList<KliensConn>();
    ServerSocket serverSocket;
    /*@FXML
    CheckBox konyhaBox;*/

    @Override
    public void stop() throws Exception {
        serverSocket.close();
    }

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) throws IOException {

        // Text area for displaying contents
        txtAreaDisplay = new TextArea();
        txtAreaDisplay.setEditable(false);
        txtAreaDisplay.setPrefHeight(300);
        txtAreaDisplay.setPrefWidth(250);
        konyhaBox = new CheckBox();
        konyhaBox.setText("Konyha");
        nappaliBox = new CheckBox();
        nappaliBox.setText("Nappali");

        RowConstraints con = new RowConstraints();
        con.setPrefHeight(30);

        GridPane gridPane = new GridPane();
        TitledPane titledPane = new TitledPane();

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(konyhaBox, nappaliBox);
        titledPane.setContent(vBox);
        titledPane.setText("Csatlakozott: ");


        gridPane.setStyle("-fx-background-color: rgb(60, 179, 113);" /*+
                "-fx-background-radius: 30;" +
                "-fx-border-radius: 30;" +
                "-fx-border-width:2;" +
                "-fx-border-color:black;"*/);
        gridPane.add(titledPane, 1, 1);
        gridPane.add(txtAreaDisplay, 2, 1);
        ColumnConstraints columnConstraints = new ColumnConstraints(40);
        gridPane.getColumnConstraints().add(columnConstraints);
        GridPane.setMargin(txtAreaDisplay, new Insets(30, 10, 10, 60));


        // Create a scene and place it in the stage
        Scene scene = new Scene(gridPane, 450, 400);
        primaryStage.setTitle("Szerver"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image("control.png"));
        primaryStage.show(); // Display the stage

        //create a new thread
        Thread t = new Thread(() -> {
            try {
                // Create a server socket
                serverSocket = new ServerSocket(Connect.port);

                //append message of the Text Area of UI (GUI Thread)
                Platform.runLater(()
                        -> txtAreaDisplay.appendText("Indítás: " + new Date() + '\n'));

                //continous loop
                while (true) {
                    // Listen for a connection request, add new connection to the list
                    Socket socket = serverSocket.accept();
                    KliensConn connection = new KliensConn(socket, this);
                    connectionList.add(connection);

                    //create a new thread
                    Thread thread = new Thread(connection);
                    thread.start();

                }
            } catch (IOException ex) {
                txtAreaDisplay.appendText(ex.toString() + '\n');
            }
        });
        t.start();

    }


    public static void main(String[] args) {
        launch(args);
    }

    public void broadcast(String message) {
        for (KliensConn clientConnection : this.connectionList) {
            clientConnection.sendMessage(message);
        }
    }
}
