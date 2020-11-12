import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class Szerver extends Application {
    /*@FXML
    TextArea txtAreaDisplay;*/
    DataInputStream inputStream;
    DataOutputStream outputStream;
    public TextArea txtAreaDisplay;
    public CheckBox konyhaBox;
    public CheckBox nappaliBox;
    public CheckBox ebedloBox;
    public CheckBox furdoBox;
    public CheckBox haloszobaBox;
    private Label felirat;
    TreeMap<String, KliensConn> connTreeMap = new TreeMap<>();
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
        txtAreaDisplay.setPrefHeight(400);
        txtAreaDisplay.setPrefWidth(330);
        konyhaBox = new CheckBox();
        konyhaBox.setText("Konyha");
        nappaliBox = new CheckBox();
        nappaliBox.setText("Nappali");
        ebedloBox = new CheckBox();
        ebedloBox.setText("Ebédlő");
        furdoBox = new CheckBox();
        furdoBox.setText("Fürdő");
        haloszobaBox = new CheckBox();
        haloszobaBox.setText("Hálószoba");
        felirat = new Label();

        RowConstraints con = new RowConstraints();
        con.setPrefHeight(30);

        GridPane gridPane = new GridPane();
        TitledPane titledPane = new TitledPane();

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(konyhaBox, nappaliBox, ebedloBox, furdoBox,haloszobaBox);
        titledPane.setContent(vBox);
        titledPane.setText("Csatlakozott: ");


        gridPane.setStyle("-fx-background-color: rgb(60, 179, 113);" +
                "-fx-background-radius: 30;" +
                "-fx-border-radius: 20;" +
                "-fx-border-width:10;" +
                "-fx-border-color:grey;");
        felirat.setText("Vezérlő panel");
        felirat.setStyle("-fx-font-size: 22px;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;");
        gridPane.add(felirat, 1, 1);
        gridPane.add(titledPane, 1, 2);
        gridPane.add(txtAreaDisplay, 2, 2);
        ColumnConstraints columnConstraints = new ColumnConstraints(40);
        gridPane.getColumnConstraints().add(columnConstraints);
        GridPane.setMargin(txtAreaDisplay, new Insets(30, 10, 10, 60));
        GridPane.setMargin(titledPane, new Insets(0, 20, 80, 20));
        GridPane.setMargin(felirat, new Insets(20, 0, 0, 0));


        // Create a scene and place it in the stage
        Scene scene = new Scene(gridPane, 580, 400);
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

                    String proba = "";
                    inputStream = new DataInputStream(socket.getInputStream());
                    outputStream = new DataOutputStream(socket.getOutputStream());
                    proba = inputStream.readUTF();
                    connTreeMap.put(proba, connection);

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
        for (KliensConn clientConnection : this.connTreeMap.values()) {
            clientConnection.sendMessage(message);
        }
    }
}
