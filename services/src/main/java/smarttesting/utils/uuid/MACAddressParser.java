package smarttesting.utils.uuid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MACAddressParser {

    public static final Pattern MAC_ADDRESS = Pattern.compile("((?:[A-F0-9]{1,2}[:-]){5}[A-F0-9]{1,2})|(?:0x)(\\d{12})(?:.+ETHER)", Pattern.CASE_INSENSITIVE);

    static String parse(String in) {
        Matcher m = MAC_ADDRESS.matcher(in);
        if (m.find()) {
            String g = m.group(2);
            if (g == null) {
                g = m.group(1);
            }
            return g == null ? g : g.replace('-', ':');
        }
        return null;
    }

}
