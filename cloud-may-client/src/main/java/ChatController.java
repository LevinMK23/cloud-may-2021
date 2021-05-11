import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatController implements Initializable {

    private Net net;
    public TextField input;
    public ListView<String> listView;

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        net.sendMessage(input.getText());
        input.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            net = Net.start(8189);
            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                        String msg = net.getMessage();
                        if (msg.equals("/takeFiles")) {
                            String files = net.getMessage();
                            String[] names = files.split(",");
                            Platform.runLater(() -> {
                                listView.getItems().clear();
                                listView.getItems().addAll(names);
                            });
                        } else {
                            Platform.runLater(() -> listView.getItems().add(msg));
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Exception while reading");
                }
            });
            readThread.setDaemon(true);
            readThread.start();
        } catch (IOException ioException) {
            System.err.println("Connection was broken");
        }
    }
}
