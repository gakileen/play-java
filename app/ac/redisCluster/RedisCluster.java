package ac.redisCluster;

import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by acmac on 2016/09/01.
 */
public class RedisCluster {

    public static JedisCluster jc;

    static {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7000));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7001));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7002));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7003));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7004));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7005));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7006));


        jc = new JedisCluster(jedisClusterNodes);
    }

    public static void set(String key, String value) {
        jc.set(key, value);
    }

    public static String get(String key) {
        return jc.get(key);
    }

}
