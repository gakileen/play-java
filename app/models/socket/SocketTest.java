package models.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by ac on 2017/3/7.
 */
public class SocketTest {

    public static void getIp() {
        InetAddress address = null;

        try {
            address = InetAddress.getByName("www.baidu.com");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println(address.getHostName());
        System.out.println(address.getHostAddress());
        System.out.println(address.getCanonicalHostName());
    }

    public static void webPing() {

        try {
            Socket sock = new Socket("www.baidu.com", 80);
            InetAddress addr = sock.getInetAddress();

            System.out.println(addr);
            sock.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void url() throws IOException {
        URL url = new URL("http://www.baidu.com");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(url.openStream(), "UTF-8"));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        reader.close();
    }


    public static void main(String[] args) throws IOException {
//        webPing();

        url();
    }


}
