package mainProgramms;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.io.FileInputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        try (ReadableByteChannel channel = new FileInputStream("file.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int bytesRead = channel.read(buffer);
            while (bytesRead != -1) {
                buffer.flip(); // Подготовка буфера для чтения

                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get()); // Чтение байтов из буфера
                }

                buffer.clear(); // Очистка буфера для следующего чтения
                bytesRead = channel.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}