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
import java.util.concurrent.TimeUnit;

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
                    nev = "ebedlo";
                } else if (message.equals("Furdo") && !server.furdoBox.isSelected()) {
                    server.furdoBox.setSelected(true);
                    nev = "furdo";
                } else if (message.equals("Haloszoba") && !server.furdoBox.isSelected()) {
                    server.haloszobaBox.setSelected(true);
                    nev = "haloszoba";
                } else if (message.equals("be") ) {
                    sendMessage("+" + nev);
                    String log = "Mozgás: " + nev + " " + new Date() + "\n";

                    //append message of the Text Area of UI (GUI Thread)
                    Platform.runLater(() -> {
                        server.txtAreaDisplay.appendText(log);
                    });
                } else if (message.equals("ki")) {
                    sendMessage("-" + nev);
                    String log = "Mozgás vége: " + nev + " " + new Date() + "\n\n";
                    //append message of the Text Area of UI (GUI Thread)
                    Platform.runLater(() -> {
                        server.txtAreaDisplay.appendText(log);
                    });
                } else {
                    String[] str = message.split(" ");
                    homersekletElemzo(message, str[1], Integer.parseInt(str[0]));

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


        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    public Integer kisebbVNagyobb(int homerseklet, int beallitottHomerseklet){
        if( homerseklet < beallitottHomerseklet )
        {
            return 1;
        }
        else if ( homerseklet > beallitottHomerseklet)
        {
            return 2;
        }
        else return 0;
    }

    public void homersekletElemzo(String message, String kliensNev, int kliensHomerseklet) throws InterruptedException {
        String log = "Hőmérséklet: " + kliensHomerseklet + " " + kliensNev + "\n";
        int homerseklet = kliensHomerseklet;
        int futes = 3;
        int hutes = 3;
        int beallitottHomerseklet = 0;

        if(kliensNev.equals("Konyha")) {
            futes = kisebbVNagyobb(kliensHomerseklet, (int)server.konyhaCBox.getValue());
            hutes = kisebbVNagyobb(kliensHomerseklet, (int)server.konyhaCBox.getValue());
            beallitottHomerseklet = (int)server.konyhaCBox.getValue();
        } else if(kliensNev.equals("Ebedlo")) {
            futes = kisebbVNagyobb(kliensHomerseklet, (int)server.ebedloCBox.getValue());
            hutes = kisebbVNagyobb(kliensHomerseklet, (int)server.ebedloCBox.getValue());
            beallitottHomerseklet = (int)server.ebedloCBox.getValue();
        } else if(kliensNev.equals("Haloszoba")) {
            futes = kisebbVNagyobb(kliensHomerseklet, (int)server.haloszobaCBox.getValue());
            hutes = kisebbVNagyobb(kliensHomerseklet, (int)server.haloszobaCBox.getValue());
            beallitottHomerseklet = (int)server.haloszobaCBox.getValue();
        } else if(kliensNev.equals("Furdo")) {
            futes = kisebbVNagyobb(kliensHomerseklet, (int)server.furdoCBox.getValue());
            hutes = kisebbVNagyobb(kliensHomerseklet, (int)server.furdoCBox.getValue());
            beallitottHomerseklet = (int)server.furdoCBox.getValue();
        } else if(kliensNev.equals("Nappali")) {
            futes = kisebbVNagyobb(kliensHomerseklet, (int)server.nappaliCBox.getValue());
            hutes = kisebbVNagyobb(kliensHomerseklet, (int)server.nappaliCBox.getValue());
            beallitottHomerseklet = (int)server.nappaliCBox.getValue();
        }

        if( futes == 1 )
        {
            server.txtAreaDisplay.appendText(log + "Fűtés.\n");
            sendMessage("+" + kliensNev.toLowerCase());
            while (homerseklet != beallitottHomerseklet){
                String string = "";
                homerseklet++;
                string = string + "Hőmérséklet: " + homerseklet + "\n";
                server.txtAreaDisplay.appendText(string);
                Thread.sleep(1000);
                sendMessage1(homerseklet + " " + kliensNev);
            }
            String log1 = "Hőmérséklet fűtés után: " + homerseklet + "\n";
            server.txtAreaDisplay.appendText(log1);
        }
        else if( hutes == 2 )
        {
            server.txtAreaDisplay.appendText(log + "Hűtés.\n");
            sendMessage("+" + kliensNev.toLowerCase());
            while (homerseklet != beallitottHomerseklet){
                String string = "";
                homerseklet--;
                string = string + "Hőmérséklet: " + homerseklet + "\n";
                server.txtAreaDisplay.appendText(string);
                Thread.sleep(1000);
                sendMessage1(homerseklet + " " + kliensNev);
            }
            String log1 = "Hőmérséklet hűtés után: " + homerseklet + "\n";
            server.txtAreaDisplay.appendText(log1);
        }
        else if ( hutes == 0 && futes == 0 )
        {
            sendMessage1(message);
            Platform.runLater(() -> {
                server.txtAreaDisplay.appendText(log + "Nem igényel változtatást." + "\n");
            });
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
