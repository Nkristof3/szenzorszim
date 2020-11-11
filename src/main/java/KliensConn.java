import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Time;
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
                } else if (message.equals("Ebedlo") && !server.nappaliBox.isSelected()) {
                    server.ebedloBox.setSelected(true);
                    nev = "ebédlő";
                } else if (message.equals("Furdo") && !server.furdoBox.isSelected()) {
                    server.furdoBox.setSelected(true);
                    nev = "furdo";
                } else if (message.equals("Haloszoba") && !server.furdoBox.isSelected()) {
                    server.haloszobaBox.setSelected(true);
                    nev = "haloszoba";
                }

                if (message.equals("be")) {
                    sendMessage("+" + nev);
                    String log = "Mozgás: " + nev + " " + new Date() + "\n";
                    //append message of the Text Area of UI (GUI Thread)
                    Platform.runLater(() -> {
                        server.txtAreaDisplay.appendText(log);
                    });
                } else if (message.equals("ki")) {
                    sendMessage("-" + nev);
                    String log = "Mozgás vége: " + nev + " " + new Date() + "\n";
                    //append message of the Text Area of UI (GUI Thread)
                    Platform.runLater(() -> {
                        server.txtAreaDisplay.appendText(log);
                    });
                }
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
