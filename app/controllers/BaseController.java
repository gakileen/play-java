package controllers;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import play.mvc.Controller;

public class BaseController extends Controller {

    protected static String getUid() {
        String uid = getHeaderString("X-UserId");

        if (StringUtils.isBlank(uid)) {
            String mi = getHeaderString("X-MI");
            String cm = getHeaderString("X-CM");

            if (StringUtils.isNotBlank(mi) && StringUtils.isNotBlank(cm)) {
                uid = decode(mi) + decode(cm);
            }
        }

        return uid;
    }

    protected static String getIp() {
        return getHeaderString("x-real-ip");
    }

    public static String getHeaderString(String key) {
        return TransformParam(key, request().headers());
    }

    public static String getQueryString(String key) {
        return TransformParam(key, request().queryString());
    }

    public static String getQueryString(String key, String defaultValue) {
        String result = getQueryString(key);
        return result == null ? defaultValue : result;
    }

    public static String TransformParam(String key, Map<String, String[]> params) {
        String result = null;
        if (params != null) {
            String[] str = params.get(key);
            if (str != null && str.length > 0) {
                result = str[0];
            }
        }
        return result;
    }

    @SuppressWarnings("unused")
    private static final byte[] ENCODE_TABLE = new byte[] { 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 75, 84,
            73, 83, 109, 67, 106, 110, 108, 53, 0, 0, 0, 0, 0, 0, 0, 80, 89,
            55, 71, 104, 111, 112, 122, 79, 114, 113, 101, 76, 98, 87, 51, 54,
            56, 115, 102, 52, 121, 119, 70, 90, 116, 0, 0, 0, 0, 0, 0, 57, 107,
            65, 88, 66, 68, 100, 85, 81, 48, 50, 97, 120, 105, 86, 82, 77, 74,
            78, 72, 99, 118, 103, 49, 117, 69, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private static final byte[] DECODE_TABLE = new byte[] { 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 106,
            120, 107, 80, 85, 57, 81, 67, 82, 97, 0, 0, 0, 0, 0, 0, 0, 99, 101,
            53, 102, 122, 88, 68, 116, 50, 114, 48, 77, 113, 115, 73, 65, 105,
            112, 51, 49, 104, 111, 79, 100, 66, 89, 0, 0, 0, 0, 0, 0, 108, 78,
            117, 103, 76, 84, 119, 69, 110, 54, 98, 56, 52, 55, 70, 71, 75, 74,
            83, 90, 121, 118, 87, 109, 86, 72, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    public static String decode(String s) {
        byte[] b = s.getBytes();
        byte[] r = new byte[b.length];
        for (int i = 0; i < b.length; i++) {
            if ((b[i] & 0x80) != 0 || DECODE_TABLE[b[i]] == 0) {
                r[i] = b[i];
            } else {
                r[i] = DECODE_TABLE[b[i]];
            }
        }
        return new String(r);
    }
}
