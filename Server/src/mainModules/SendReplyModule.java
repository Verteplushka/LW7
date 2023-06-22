package mainModules;

import mainProgramms.ReplyObj;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SendReplyModule implements Runnable {
    private ReplyObj replyObj;
    private SocketChannel clientSocketChannel;

    public SendReplyModule(SocketChannel clientSocketChannel, ReplyObj replyObj) {
        this.replyObj = replyObj;
        this.clientSocketChannel = clientSocketChannel;
    }

    @Override
    public void run() {
        try {
            ByteArrayOutputStream byteStream2 = new ByteArrayOutputStream();
            ObjectOutputStream objectStream2 = new ObjectOutputStream(byteStream2);
            objectStream2.writeObject(replyObj);
            objectStream2.flush();

            ByteBuffer writingBuffer = ByteBuffer.wrap(byteStream2.toByteArray());

            clientSocketChannel.write(writingBuffer);

            System.out.println("Отправлено сообщение клиенту " + clientSocketChannel.getRemoteAddress() + ": " + replyObj.getJson());
        } catch (IOException exception) {
            System.out.println("Проблемы с соединением");
        }
    }
}
