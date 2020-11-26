import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class TaskRead implements Runnable {
    //private variables
    Socket socket;
    public Konyha client;
    Nappali client2;
    Ebedlo client3;
    Furdo client4;
    Haloszoba client5;
    DataInputStream input;
    int homerseklet;

    //constructor
    public TaskRead(Socket socket, Konyha client) {
        this.socket = socket;
        this.client = client;
    }

    public TaskRead(Socket socket, Nappali client2){
        this.socket = socket;
        this.client2 = client2;
    }

    public TaskRead(Socket socket, Ebedlo client3){
        this.socket = socket;
        this.client3 = client3;
    }

    public TaskRead(Socket socket, Furdo client4) {
        this.socket = socket;
        this.client4 = client4;
    }

    public TaskRead(Socket socket, Haloszoba client5) {
        this.socket = socket;
        this.client5 = client5;
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
                //System.out.println("Taskread: " + message);

                if (message.equals("+konyha")) {
                    client.scrollPane.setStyle("-fx-border-color: yellow;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 1.0");

                    //append message of the Text Area of UI (GUI Thread)

                    if( homerseklet != 0 ){
                    String kiirat = "Mozgás. " + new Date() + "\n" + "Hőmérséklet: " + homerseklet + "\n";
                    Platform.runLater(() -> {
                        //display the message in the textarea
                        client.txtAreaDisplay.appendText(kiirat);
                    });}
                }
                else if (message.equals("+nappali")){
                    client2.scrollPane.setStyle("-fx-border-color: yellow;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 1.0");
                    //String kiirat = "Mozgás. " + new Date() + "\n";
                    if( homerseklet != 0 ){
                    String kiirat = "Mozgás. " + new Date() + "\n" + "Hőmérséklet: " + homerseklet + "\n";
                    Platform.runLater(() -> {
                        //display the message in the textarea
                        client2.txtAreaDisplay.appendText(kiirat);
                    });}
                }
                else if (message.equals("+ebedlo")){
                    client3.scrollPane.setStyle("-fx-border-color: yellow;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 1.0");
                    //String kiirat = "Mozgás. " + new Date() + "\n";
                    if( homerseklet != 0 ){
                    String kiirat = "Mozgás. " + new Date() + "\n" + "Hőmérséklet: " + homerseklet + "\n";
                    Platform.runLater(() -> {
                        //display the message in the textarea
                        client3.txtAreaDisplay.appendText(kiirat);
                    });}
                }
                else if (message.equals("-konyha")){
                    client.scrollPane.setStyle("-fx-border-color: black;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 0.5");
                    String kiirat = "Mozgás vége. " + new Date() + "\n";
                    Platform.runLater(() -> {
                        //display the message in the textarea
                        client.txtAreaDisplay.appendText(kiirat);
                    });
                }
                else if (message.equals("-nappali")){
                    client2.scrollPane.setStyle("-fx-border-color: black;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 0.5");
                    String kiirat = "Mozgás vége. " + new Date() + "\n";
                    Platform.runLater(() -> {
                        //display the message in the textarea
                        client2.txtAreaDisplay.appendText(kiirat);
                    });
                }
                else if (message.equals("-ebedlo")){
                    client3.scrollPane.setStyle("-fx-border-color: black;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 0.5");
                    String kiirat = "Mozgás vége. " + new Date() + "\n";
                    Platform.runLater(() -> {
                        //display the message in the textarea
                        client3.txtAreaDisplay.appendText(kiirat);
                    });
                } else if (message.equals("+furdo")){
                    client4.scrollPane.setStyle("-fx-border-color: yellow;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 1.0");
                    //String kiirat = "Mozgás. " + new Date() + "\n";
                    if( homerseklet != 0 ){
                    String kiirat = "Mozgás. " + new Date() + "\n" + "Hőmérséklet: " + homerseklet + "\n";
                    Platform.runLater(() -> {
                        //display the message in the textarea
                        client4.txtAreaDisplay.appendText(kiirat);
                    });}
                } else if (message.equals("-furdo")){
                    client4.scrollPane.setStyle("-fx-border-color: black;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 0.5");
                    String kiirat = "Mozgás vége. " + new Date() + "\n";
                    Platform.runLater(() -> {
                        //display the message in the textarea
                        client4.txtAreaDisplay.appendText(kiirat);
                    });
                } else if (message.equals("+haloszoba")) {
                    client5.scrollPane.setStyle("-fx-border-color: yellow;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 1.0");

                    if( homerseklet != 0 ){
                    String kiirat = "Mozgás. " + new Date() + "\n" + "Hőmérséklet: " + homerseklet + "\n";
                    Platform.runLater(() -> {
                        //display the message in the textarea
                        client5.txtAreaDisplay.appendText(kiirat);
                    });}
                } else if (message.equals("-haloszoba")) {
                    client5.scrollPane.setStyle("-fx-border-color: black;" +
                            "-fx-border-width: 15;" +
                            "-fx-opacity: 0.5");
                    String kiirat = "Mozgás vége. " + new Date() + "\n";
                    Platform.runLater(() -> {
                        //display the message in the textarea
                        client5.txtAreaDisplay.appendText(kiirat);
                    });
                } else if (message.equals("e-")){
                    client3.stop();
                } else if (message.equals("k-")){
                    client.stop();
                } else if (message.equals("n-")){
                    client2.stop();
                } else if (message.equals("f-")){
                    client4.stop();
                } else if (message.equals("h-")){
                    client5.stop();
                }else
                {
                    String[] hom = message.split(" ");
                    homerseklet = Integer.parseInt(hom[0]);
                    if(hom[1].equals("Konyha")){
                        client.homerseklet = homerseklet;
                    } else if (hom[1].equals("Nappali")){
                        client2.homerseklet = homerseklet;
                    } else if (hom[1].equals("Ebedlo")){
                        client3.homerseklet = homerseklet;
                    } else if (hom[1].equals("Furdo")){
                        client4.homerseklet = homerseklet;
                    } else if (hom[1].equals("Haloszoba")){
                        client5.homerseklet = homerseklet;
                    }
                }

            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
