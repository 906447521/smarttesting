package smarttesting.utils.sso;

/**
 *
 */
public class Arguments {

    public static String getEncoding() {
        return "UTF-16LE";
    }

    public static String getAlgorithm() {
        return "AES";
    }

    public static int getKeySize() {
        return 128;
    }

    public static String getValidationKey() {
        return "282487E295028E59B8F411ACB689CCD6F39DDD21E6055A3EE480424315994760ADF21B580D8587DB675FA02F79167413044E25309CCCDB647174D5B3D0DD9141";
    }

    public static String getDecryptionKey() {
        return "8B6697227CBCA902B1A0925D40FAA00B353F2DF4359D2099";
    }

    public static String getTransformation() {
        if (getKeySize() == 128) {
            return "AES";
        } else {
            return "AES/CBC/PKCS5Padding";
        }
    }

}
