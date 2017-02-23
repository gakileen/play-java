package models.geo;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import common.utils.Log;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ac on 2017/2/21.
 */
public class GeoUtils {

    private static final String TAG = GeoUtils.class.getSimpleName();

    private static DatabaseReader cityReader;

    static {
        try {
            Log.i(TAG, "init start...");

            // A File object pointing to your GeoIP2 or GeoLite2 database
            File database = new File("/usr/local/data/geoip/GeoLite2-City.mmdb");

            // This creates the DatabaseReader object, which should be reused across lookups.
            cityReader = new DatabaseReader.Builder(database).withCache(new CHMCache()).build();

            Log.i(TAG, "init success!");
        } catch (Exception e) {
            Log.i(TAG, "init failed.", e);
        }
    }

    public static String getCityName(String ip) {
        String cityName = null;

        if (cityReader != null) {
            try {
                cityName = cityReader.city(InetAddress.getByName(ip)).getCity().getName();
            } catch (Exception e) {}
        }

        return StringUtils.isBlank(cityName) ? "-unknown-" : cityName;
    }

    public static String getCountryCode(String ip) {
        String countryCode = null;

        if (cityReader != null) {
            try {
                countryCode = cityReader.city(InetAddress.getByName(ip)).getCountry().getIsoCode();
            } catch (Exception e) {}
        }

        return StringUtils.isBlank(countryCode) ? "-unknown-" : countryCode;
    }

    public static void main(String[] args) throws Exception {

        String fileName = "indiaIps10000";

        File file = new File(String.format("/Users/ac/xxx/%s.txt", fileName));

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        FileWriter fw = new FileWriter(String.format("/Users/ac/xxx/%s_result.txt", fileName));

        String line;

        List<GeoInfo> list = new ArrayList<>();

        while ( (line = br.readLine()) != null) {
            list.add(new GeoInfo(line, getCountryCode(line), getCityName(line)));
        }

        fw.write("================summary================\n");

        list.stream().collect(Collectors.groupingBy(geoinfo -> geoinfo.cityName, Collectors.counting())).entrySet()
                .stream()
                .sorted((o1, o2) -> {
                    int sort = o2.getValue().compareTo(o1.getValue());

                    return sort == 0 ? o1.getKey().compareTo(o2.getKey()) : sort;
                }).forEach(entry -> {
                    try {
                        fw.write(entry.getKey() +
                                StringUtils.repeat(" ", Math.max(2, 24 - entry.getKey().length())) +
                                entry.getValue() + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        fw.write("\n\n\n");

        fw.write("================detail================\n");

        list.stream().sorted((GeoInfo o1, GeoInfo o2) -> {
            int sort = o1.cityName.compareTo(o2.cityName);

            if (sort != 0) {
                return sort;
            }

            sort = o1.countryCode.compareTo(o2.countryCode);

            if (sort != 0) {
                return sort;
            }

            return o1.ip.compareTo(o2.ip);

        }).forEach(geoinfo -> {
            try {
//                System.out.println(geoinfo);
                fw.write(geoinfo.toString() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fw.flush();
        fw.close();
        br.close();
    }

    public static void main2(String[] args) throws Exception {
        InetAddress ipAddress = InetAddress.getByName("1.187.110.124");

        // Replace "city" with the appropriate method for your database, e.g., "country".
        CityResponse response = cityReader.city(ipAddress);

        Country country = response.getCountry();
        System.out.println(country.getIsoCode());            // 'US'
        System.out.println(country.getName());               // 'United States'
        System.out.println(country.getNames().get("zh-CN")); // '美国'

        Subdivision subdivision = response.getMostSpecificSubdivision();
        System.out.println(subdivision.getName());    // 'Minnesota'
        System.out.println(subdivision.getIsoCode()); // 'MN'

        City city = response.getCity();
        System.out.println(city.getName()); // 'Minneapolis'

        Postal postal = response.getPostal();
        System.out.println(postal.getCode()); // '55455'

        Location location = response.getLocation();
        System.out.println(location.getLatitude());  // 44.9733
        System.out.println(location.getLongitude()); // -93.2323
    }

    public static class GeoInfo {
        String ip;
        String countryCode;
        String cityName;

        public GeoInfo(String ip, String countryCode, String cityName) {
            this.ip = ip;
            this.countryCode = countryCode;
            this.cityName = cityName;
        }

        @Override
        public String toString() {
            return ip + StringUtils.repeat(" ", 6 + (15 - ip.length())) + countryCode + "      " + cityName;
        }
    }

}
