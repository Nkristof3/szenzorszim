import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class TaskRead implements Runnable {
    //private variables
    Socket socket;
    Kliens client;
    Kliens2 client2;
    DataInputStream input;

    //constructor
    public TaskRead(Socket socket, Kliens client) {
        this.socket = socket;
        this.client = client;
    }

    public TaskRead(Socket socket, Kliens2 client2){
        this.socket = socket;
        this.client2 = client2;
    }

    @Override
    public void run() {
        //continuously loop it
        while (true) {
            try {
                //Create data input stream
                input = new DataInputStream(socket.getInputStream());

                //get input from the server
                String message = input.readUTF();

                if (message.equals("+konyha")) {
                    client.scrollPane.setStyle("-fx-border-color: yellow;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 1.0");

                    //append message of the Text Area of UI (GUI Thread)
                    String kiirat = "Mozgás. " + new Date() + "\n";
                    Platform.runLater(() -> {
                        //display the message in the textarea
                        client.txtAreaDisplay.appendText(kiirat);
                    });
                }
                else if (message.equals("+nappali")){
                    client2.scrollPane.setStyle("-fx-border-color: yellow;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 1.0");
                    String kiirat = "Mozgás. " + new Date() + "\n";
                    Platform.runLater(() -> {
                        //display the message in the textarea
                        client2.txtAreaDisplay.appendText(kiirat);
                    });
                }

            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
