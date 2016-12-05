package models.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by acmac on 16/3/11.
 */
public class ServerSocketChannelTest {

    public static void ServerSocket() throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(9999));

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            System.out.println("-------------new request------------");

            ByteBuffer buf = ByteBuffer.allocate(64);

            int bytesRead = socketChannel.read(buf);

            while (bytesRead != -1) {
                buf.flip();

                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }

                buf.clear();

                bytesRead = socketChannel.read(buf);
            }

            System.out.println();
        }

    }

    public static void main (String[] args) throws Exception {
        ServerSocket();
    }
}
