package ac.geoip;

import com.maxmind.geoip.*;
import java.io.IOException;

public class CountryLookup {
	
    public static void main(String[] args) {
		try {
		    String sep = System.getProperty("file.separator");
	
		    // Uncomment for windows
		    // String dir = System.getProperty("user.dir");
	
		    // Uncomment for Linux
		    String dir = "/usr/local/share/GeoIP";
	
		    String dbfile = dir + sep + "GeoIP.dat";
		    // You should only call LookupService once, especially if you use
		    // GEOIP_MEMORY_CACHE mode, since the LookupService constructor takes up
		    // resources to load the GeoIP.dat file into memory
		    //LookupService cl = new LookupService(dbfile,LookupService.GEOIP_STANDARD);
		    LookupService cl = new LookupService(dbfile, LookupService.GEOIP_MEMORY_CACHE);
	
		    System.out.println(cl.getCountry("151.38.39.114").getCode());
		    System.out.println(cl.getCountry("151.38.39.114").getName());
		    System.out.println(cl.getCountry("58.240.66.58").getName());
		    System.out.println(cl.getCountry("106.185.47.149").getName());
		    System.out.println(cl.getCountry("200.21.225.82").getName());
	
		    cl.close();
		}
		catch (IOException e) {
		    System.out.println("IO Exception");
		}
    }

}
