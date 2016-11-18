package ac.nio;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * Created by acmac on 16/3/14.
 */
public class PipeTest {

    public static void test () throws Exception {
        Pipe pipe = Pipe.open();

        Pipe.SinkChannel sinkChannel = pipe.sink();

        String newData = "New string to send to file..." + System.currentTimeMillis();
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());

        buf.flip();

        while (buf.hasRemaining()) {
            int bytesSend = sinkChannel.write(buf);
            System.out.println(bytesSend);
        }

        Pipe.SourceChannel sourceChannel = pipe.source();

        ByteBuffer buf2 = ByteBuffer.allocate(48);
        int bytesRead = sourceChannel.read(buf2);

        while (bytesRead != -1) {
            buf2.flip();

            while (buf2.hasRemaining()) {
                System.out.print((char) buf2.get()) ;
            }

            buf2.clear();

            bytesRead = sourceChannel.read(buf2);
        }

    }

    public static void main (String[] args) throws Exception {
        test();
    }

}
