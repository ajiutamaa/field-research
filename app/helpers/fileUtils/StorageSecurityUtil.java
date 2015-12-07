package helpers.fileUtils;

import org.apache.commons.codec.binary.Base32;

/**
 * Created by lenovo on 07/12/2015.
 */
public class StorageSecurityUtil {
    static Base32 enc = new Base32();
    static Base32 dec = new Base32();

    public static String encodeToString(String input) {
        int length = input.length();
        String toEncode = input + "zzzzz";
        toEncode = toEncode.substring(0, length-(length%5)+5);
        String encoded = enc.encodeToString(toEncode.getBytes());
        length = encoded.length();
        encoded = encoded + "zzzzz";
        encoded = encoded.substring(0, length-(length%5)+5);
        encoded = enc.encodeToString(encoded.getBytes());
        return encoded;
    }

    public static String decodeToString(String encoded) {
        String temp = new String(dec.decode(encoded));
        temp = temp.replace("z","");
        temp = new String(dec.decode(temp));
        temp = temp.replace("z", "");
        return temp;
    }
}
