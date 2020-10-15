import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class KliensConn implements Runnable {
    Socket socket;
    Szerver server;
    // Create data input and output streams
    DataInputStream input;
    DataOutputStream output;
    String nev = "";

    public KliensConn(Socket socket, Szerver server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {


        try {
            // Create data input and output streams
            input = new DataInputStream(
                    socket.getInputStream());
            output = new DataOutputStream(
                    socket.getOutputStream());

            while (true) {
                // Get message from the client
                String message = input.readUTF();

                if (message.equals("Konyha") && !server.konyhaBox.isSelected()) {
                    server.konyhaBox.setSelected(true);
                    nev = "konyha";
                } else if (message.equals("Nappali") && !server.nappaliBox.isSelected()) {
                    server.nappaliBox.setSelected(true);
                    nev = "nappali";
                }

                if (message.equals("be")) {
                    sendMessage("+");
                    String log = "Mozgás: " + nev + " " + "\n";
                    //append message of the Text Area of UI (GUI Thread)
                    Platform.runLater(() -> {
                        server.txtAreaDisplay.appendText(log);
                    });
                }


                //send message via server broadcast
                //server.broadcast(message);
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    //send message back to client
    public void sendMessage(String message) {
        try {
            output.writeUTF(message);
            output.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
