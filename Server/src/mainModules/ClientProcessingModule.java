package mainModules;


import java.io.*;
import java.net.InetSocketAddress;

import com.google.gson.JsonSyntaxException;
import mainProgramms.*;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientProcessingModule implements Runnable {
    private static final int BUFFER_SIZE = Integer.MAX_VALUE/10;
    private static final int PORT_NAME = 8123;
    private static Selector selector;
    private static ServerSocketChannel serverSocket;
    private static ByteBuffer buffer;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    @Override
    public void run() {
        try {

            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.configureBlocking(false);
            buffer = ByteBuffer.allocate(BUFFER_SIZE);

            serverSocket.bind(new InetSocketAddress(PORT_NAME));
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            System.out.println("Сервер запущен, ожидание клиента...");


            while (true) {

                if (selector.selectNow() == 0) {
                    continue;
                }

                Iterator<SelectionKey> keyIterator = ConnectionModule.connect(selector);
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel clientSocketChannel = serverSocketChannel.accept();

                        clientSocketChannel.configureBlocking(false);

                        clientSocketChannel.register(selector, SelectionKey.OP_READ);

                        System.out.println("Подключился клиент: " + clientSocketChannel.getRemoteAddress());
                    } else if (key.isReadable()) {
                        SingleClientProcessingModule singleClientProcessingModule = new SingleClientProcessingModule(key, CollectionManager.getSpaceMarines());
                        executor.execute(singleClientProcessingModule);
                    }
                }
            }
        } catch (IOException exception) {
            System.out.println("Проблемы с соединением");
        } catch (JsonSyntaxException | NullPointerException exception) {
            System.out.println("Передан некорректный запрос");
        }
    }
}