package mainModules;

import collectionClasses.SpaceMarine;
import com.google.gson.JsonSyntaxException;
import mainProgramms.ReplyObj;
import mainProgramms.RequestObj;

import java.io.*;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class SingleClientProcessingModule implements Runnable {
    private final int BUFFER_SIZE = Integer.MAX_VALUE / 100;
    private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    private final SelectionKey selectionKey;
    private final LinkedList<SpaceMarine> spaceMarines;
    private boolean flag;

    public SingleClientProcessingModule(SelectionKey selectionKey, LinkedList<SpaceMarine> spaceMarines) {
        this.selectionKey = selectionKey;
        this.spaceMarines = spaceMarines;
    }

    @Override
    public void run() {
        SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
        try {
            buffer.clear();
            buffer.put(new byte[BUFFER_SIZE]);
            buffer.clear();
            int bytesRead = clientSocketChannel.read(buffer);
            if (bytesRead > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                ByteArrayInputStream byteStream = new ByteArrayInputStream(buffer.array());
                ObjectInputStream objectStream = new ObjectInputStream(byteStream);
                RequestObj recievedObj = (RequestObj) objectStream.readObject();
                System.out.println("Получено сообщение от клиента " + clientSocketChannel.getRemoteAddress() + ": " + recievedObj);

                ReplyObj replyObj = ExecutionCommandManager.execute(spaceMarines, recievedObj);

                SendReplyManager.send(clientSocketChannel, replyObj);
            } else if (bytesRead < 0) {
                System.out.println("Клиент " + clientSocketChannel.getRemoteAddress() + " отключился");
                selectionKey.cancel();
                clientSocketChannel.close();
            }
        } catch (SocketException exception) {
            try {
                System.out.println("Клиент " + clientSocketChannel.getRemoteAddress() + " отключился");
                selectionKey.cancel();
                clientSocketChannel.close();
            } catch (IOException e) {
            }
        } catch (IOException exception) {
            System.out.println("Проблемы с соединением");
        } catch (JsonSyntaxException | NullPointerException exception) {
            System.out.println("Передан некорректный запрос");
        } catch (ClassNotFoundException exception) {
            System.out.println("Передан объект некорректного класса");
        }
    }

    public boolean isFlag() {
        return this.flag;
    }
}
