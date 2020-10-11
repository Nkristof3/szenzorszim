import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TaskRead implements Runnable {
    //private variables
    Socket socket;
    Kliens client;
    DataInputStream input;

    //constructor
    public TaskRead(Socket socket, Kliens client) {
        this.socket = socket;
        this.client = client;
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

                if (message.equals("+")) {
                    client.scrollPane.setStyle("-fx-border-color: yellow;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 1.0");
                }

                //append message of the Text Area of UI (GUI Thread)
                Platform.runLater(() -> {
                    //display the message in the textarea
                    client.txtAreaDisplay.appendText(message + "\n");
                });
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
