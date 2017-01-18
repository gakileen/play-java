package helpers;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import common.utils.Log;

import java.io.File;
import java.net.InetAddress;

/**
 * Created by ac on 2017/1/18.
 */
public class CountryUtils {

    private static final String TAG = CountryUtils.class.getSimpleName();

    private static DatabaseReader reader;

    static {
        try {
            Log.i(TAG, "init start...");

            // A File object pointing to your GeoIP2 or GeoLite2 database
            File database = new File("/usr/local/data/geoip/GeoLite2-Country.mmdb");

            // This creates the DatabaseReader object, which should be reused across lookups.
            reader = new DatabaseReader.Builder(database).withCache(new CHMCache()).build();

            Log.i(TAG, "init success!");
        } catch (Exception e) {
            Log.i(TAG, "init failed.", e);
        }
    }

    public static String getCountryCodeFromIp(String ip) {

        String cc = "---";

        if (reader != null) {
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);

                CountryResponse response = reader.country(ipAddress);

                cc = response.getCountry().getIsoCode();
            } catch (Exception e) {}
        }

        return cc;
    }

    public static void main(String[] args) {
        System.out.println(getCountryCodeFromIp("103.69.247.65"));
    }

}
