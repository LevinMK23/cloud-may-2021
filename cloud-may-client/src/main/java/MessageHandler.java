import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Message;

public class MessageHandler extends SimpleChannelInboundHandler<Message> {

    private Callback callback;

    public MessageHandler(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        callback.processMessage(message);
    }
}
