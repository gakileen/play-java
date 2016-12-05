package models.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import javax.inject.Singleton;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by acmac on 2016/11/18.
 */

@Singleton
public class ESClient {

    private static final String TAG = ESTest.class.getSimpleName();

    private TransportClient client;

    public ESClient() {
        try {
//            Log.i(TAG, "ESClient", "init start");
            Settings settings = Settings.settingsBuilder()
                    .put("cluster.name", "dmcluster")
                    .put("client.transport.sniff", true)
                    .build();

            client = TransportClient.builder().settings(settings).build();

            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.0.93"), 19303));
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.0.94"), 19304));
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.0.95"), 19305));
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.0.96"), 19306));
//            Log.i(TAG, "ESClient", "init end");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public TransportClient getTransportClient() {
        return client;
    }

    public void close() {
        client.close();
    }

}
