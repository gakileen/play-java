package models.Launcher;

import java.net.URL;

/**
 * Created by ac on 2017/1/24.
 */
public class LauncherTest {

    public static void bootstrapClassPath1() {
        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (URL url : urls) {
            System.out.println(url.toExternalForm());
        }
    }

    public static void bootstrapClassPath2() {
        System.out.println(System.getProperty("sun.boot.class.path"));
    }

    public static void main(String[] args) {




    }

}
