package models.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * Created by acmac on 16/3/8.
 */
public class NioTest {

    public static void byteBufferTest() throws Exception {

        RandomAccessFile aFile = new RandomAccessFile("/Users/acmac/xxx/1.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);

        while (bytesRead != -1) {
//            System.out.println("Read " + bytesRead);
            buf.flip();

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }

            buf.clear();

//            System.out.println();

            bytesRead = inChannel.read(buf);
        }

        aFile.close();
    }


    public static void byteBufferTest2() throws Exception {

        RandomAccessFile aFile = new RandomAccessFile("/Users/acmac/xxx/1.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf1 = ByteBuffer.allocate(48);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);

        ByteBuffer[] byteBuffers = {buf1, buf2};
        inChannel.read(byteBuffers);

        buf1.flip();
        while (buf1.hasRemaining()) {
            System.out.print((char) buf1.get());
        }

        System.out.println();

        buf2.flip();
        while (buf2.hasRemaining()) {
            System.out.print((char) buf2.get());
        }


        aFile.close();
    }

    public static void transferFrom() throws Exception {
        RandomAccessFile fromFile = new RandomAccessFile("/Users/acmac/xxx/1.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("/Users/acmac/xxx/2.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

//        toChannel.transferFrom(fromChannel, 0l, fromChannel.size());

        fromChannel.transferTo(0, fromChannel.size(), toChannel);

    }


    public static void selector() throws Exception {

        Selector selector = Selector.open();

        SocketChannel channel = null;

        channel.configureBlocking(false);

        SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_READ);

        int interestSet = selectionKey.interestOps();

        int readySet = selectionKey.readyOps();

        selector.select();

    }







    public static void main(String[] args) throws Exception{



    }
}
