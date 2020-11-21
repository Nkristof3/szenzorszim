import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

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
    public ComboBox konyhaCBox;
    public ComboBox nappaliCBox;
    public ComboBox ebedloCBox;
    public ComboBox furdoCBox;
    public ComboBox haloszobaCBox;
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
        konyhaBox.setMinWidth(80);
        nappaliBox = new CheckBox();
        nappaliBox.setMinWidth(80);
        nappaliBox.setText("Nappali");
        ebedloBox = new CheckBox();
        ebedloBox.setText("Ebédlő");
        ebedloBox.setMinWidth(80);
        furdoBox = new CheckBox();
        furdoBox.setText("Fürdő");
        furdoBox.setMinWidth(80);
        haloszobaBox = new CheckBox();
        haloszobaBox.setText("Hálószoba");
        haloszobaBox.setMinWidth(80);

        ObservableList<Integer> oList = FXCollections.observableArrayList(16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);
        konyhaCBox = new ComboBox();
        konyhaCBox.setItems(oList);
        konyhaCBox.setMinWidth(55);
        konyhaCBox.getSelectionModel().select(6);

        nappaliCBox = new ComboBox();
        nappaliCBox.setItems(oList);
        nappaliCBox.setMinWidth(55);
        nappaliCBox.getSelectionModel().select(6);

        ebedloCBox = new ComboBox();
        ebedloCBox.setItems(oList);
        ebedloCBox.setMinWidth(55);
        ebedloCBox.getSelectionModel().select(6);

        furdoCBox = new ComboBox();
        furdoCBox.setItems(oList);
        furdoCBox.setMinWidth(55);
        furdoCBox.getSelectionModel().select(6);

        haloszobaCBox = new ComboBox();
        haloszobaCBox.setItems(oList);
        haloszobaCBox.setMinWidth(55);
        haloszobaCBox.getSelectionModel().select(6);

        felirat = new Label();
        ColumnConstraints columnConstraints = new ColumnConstraints(40);

        RowConstraints con = new RowConstraints();
        con.setPrefHeight(30);

        GridPane gridPane = new GridPane();
        TitledPane titledPane = new TitledPane();

        VBox vBox = new VBox(15);
        HBox hbox1 = new HBox(konyhaBox, konyhaCBox);
        hbox1.setSpacing(40);
        HBox hbox2 = new HBox(nappaliBox, nappaliCBox);
        hbox2.setSpacing(40);
        HBox hbox3 = new HBox(ebedloBox, ebedloCBox);
        hbox3.setSpacing(40);
        HBox hbox4 = new HBox(furdoBox, furdoCBox);
        hbox4.setSpacing(40);
        HBox hbox5 = new HBox(haloszobaBox, haloszobaCBox);
        hbox5.setSpacing(40);
        vBox.getChildren().addAll(hbox1, hbox2, hbox3, hbox4, hbox5);
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
        gridPane.getColumnConstraints().add(columnConstraints);
        GridPane.setMargin(txtAreaDisplay, new Insets(0, 20, 20, 60));
        GridPane.setMargin(titledPane, new Insets(0, 20, 80, 0));
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
                    //System.out.println("PROBA" + proba);
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
