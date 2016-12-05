package models.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by acmac on 16/3/11.
 */
public class SocketChannelTest {

    public static void socketChannel() throws Exception {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));

        while (! socketChannel.finishConnect()) {
            System.out.println("waiting...");
            Thread.currentThread().sleep(1000);
        }


        String newData = "New string to write to file..." + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(64);
        buf.clear();

        buf.put(newData.getBytes());

        buf.flip();

        while (buf.hasRemaining()) {
            socketChannel.write(buf);
        }


        socketChannel.close();
    }

    public static void main (String[] args) throws Exception {
        socketChannel();
    }
}
