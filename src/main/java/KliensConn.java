import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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

                if (message.equals("be") ) {
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

                char[] str = message.toCharArray();
                if( Character.isDigit(str[0]))
                {
                    String log = "Hőmérséklet: " + message + "\n";

                    int homerseklet = Integer.parseInt(message);
                    int futes = 0;
                    int hutes = 0;

                    if( homerseklet < 20 )
                    {
                        futes = 1;
                        while(homerseklet != 20 )
                            homerseklet++;

                    }
                    else if ( homerseklet > 28 )
                    {
                        hutes = 1;
                        while( homerseklet != 24 )
                            homerseklet--;

                    }


                    if( futes == 1 )
                    {
                        String log1 = "Hőmérséklet fűtés után: " + homerseklet + "\n";
                        sendMessage1(homerseklet + " " + message);
                        //append message of the Text Area of UI (GUI Thread)
                        Platform.runLater(() -> {
                            server.txtAreaDisplay.appendText(log);
                            server.txtAreaDisplay.appendText(log1);
                        });
                    }
                    else if( hutes == 1 )
                    {
                        String log1 = "Hőmérséklet hűtés után: " + homerseklet + "\n";
                        sendMessage1(homerseklet + " " + message );
                        //append message of the Text Area of UI (GUI Thread)
                        Platform.runLater(() -> {
                            server.txtAreaDisplay.appendText(log);
                            server.txtAreaDisplay.appendText(log1);
                        });
                    }
                    else if ( hutes != 1 && futes != 1 )
                    {
                        sendMessage1(message);
                        Platform.runLater(() -> {
                            server.txtAreaDisplay.appendText(log + "\n" + "Nem igényel változtatást. \n");
                        });
                    }
                }

                server.ebedloBox.selectedProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        KliensConn conn = server.connTreeMap.get("Ebedlo");
                        server.connTreeMap.remove("Ebedlo");
                        sendMessage("e-");
                    }
                });
                server.konyhaBox.selectedProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        KliensConn conn = server.connTreeMap.get("Konyha");
                        server.connTreeMap.remove("Konyha");
                        sendMessage("k-");
                    }
                });
                server.nappaliBox.selectedProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        KliensConn conn = server.connTreeMap.get("Nappali");
                        server.connTreeMap.remove("Nappali");
                        sendMessage("n-");
                    }
                });
                server.furdoBox.selectedProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        KliensConn conn = server.connTreeMap.get("Furdo");
                        sendMessage("f-");
                        server.connTreeMap.remove("Furdo");
                    }
                });
                server.haloszobaBox.selectedProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        KliensConn conn = server.connTreeMap.get("Haloszoba");
                        server.connTreeMap.remove("Haloszoba");
                        sendMessage("h-");
                    }
                });
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

    public void sendMessage1(String message) {
        try {
            output.writeUTF(message);
            output.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
