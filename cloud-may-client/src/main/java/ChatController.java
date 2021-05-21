import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Message;

public class ChatController implements Initializable {

    public TextField input;
    public ListView<String> listView;
    ObjectDecoderInputStream is;
    ObjectEncoderOutputStream os;
    private NettyNetwork network;

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        Message message = Message.builder()
                .content(input.getText())
                .author("user")
                .sendAt(System.currentTimeMillis())
                .build();
        network.writeMessage(message);
        input.clear();
    }

    private Callback getCallback() {
        return message -> Platform.runLater(() -> listView.getItems().add(message.getAuthor() + ": " + message.getContent()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = NettyNetwork.getInstance();
        network.setCallback(getCallback());
        Thread thread = new Thread(network);
        thread.setDaemon(true);
        thread.start();
    }
}
