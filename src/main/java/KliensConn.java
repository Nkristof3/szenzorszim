import javafx.application.Platform;
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

                server.ebedloBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if(!server.ebedloBox.isSelected()){
                            if(server.connTreeMap.containsKey("Ebedlo")){
                                KliensConn conn = server.connTreeMap.get("Ebedlo");
                                server.connTreeMap.remove("Ebedlo");
                                sendMessage("e-");
                                //conn.socket.close();
                            }
                        }
                    }
                });
                server.konyhaBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if(!server.konyhaBox.isSelected()){
                            if(server.connTreeMap.containsKey("Konyha")){
                                KliensConn conn = server.connTreeMap.get("Konyha");
                                server.connTreeMap.remove("Konyha");
                                sendMessage("k-");
                                //conn.socket.close();
                            }
                        }
                    }
                });
                server.nappaliBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if(!server.nappaliBox.isSelected()){
                            if(server.connTreeMap.containsKey("Nappali")){
                                KliensConn conn = server.connTreeMap.get("Nappali");
                                server.connTreeMap.remove("Nappali");
                                try {
                                    sendMessage("n-");
                                    conn.socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

                server.furdoBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if(!server.furdoBox.isSelected()){
                            if(server.connTreeMap.containsKey("Furdo")){
                                KliensConn conn = server.connTreeMap.get("Furdo");
                                server.connTreeMap.remove("Furdo");
                                try {
                                    sendMessage("f-");
                                    conn.socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                server.haloszobaBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if(!server.haloszobaBox.isSelected()){
                            if(server.connTreeMap.containsKey("Haloszoba")){
                                KliensConn conn = server.connTreeMap.get("Haloszoba");
                                server.connTreeMap.remove("Haloszoba");
                                try {
                                    sendMessage("h-");
                                    conn.socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
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
}
