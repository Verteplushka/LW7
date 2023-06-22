package mainModules;

import mainProgramms.ReplyObj;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendReplyManager {
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    public static void send(SocketChannel socketChannel, ReplyObj replyObj){
        SendReplyModule sendReplyModule = new SendReplyModule(socketChannel, replyObj);
        executor.execute(sendReplyModule);
    }
}
