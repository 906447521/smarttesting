package smarttesting.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5 {

    public static String encrypt(String context) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(context.getBytes());
            byte bytes[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < bytes.length; offset++) {
                i = bytes[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();

        } catch (NoSuchAlgorithmException e) {
        }
        return result;
    }


    public static void main(String[] args) {
        System.out.println(MD5.encrypt("xujia333"));
    }
}
