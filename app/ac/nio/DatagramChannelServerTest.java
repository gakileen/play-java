package ac.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Created by acmac on 16/3/14.
 */
public class DatagramChannelServerTest {

    public static void datagram () throws Exception {

        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(9999));

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();

        channel.receive(buf);

        buf.flip();

        while (buf.hasRemaining()) {
            System.out.print((char) buf.get());
        }

        buf.clear();

    }

    public static void main(String[] args) throws Exception {
        datagram();
    }
}
