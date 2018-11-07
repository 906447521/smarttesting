package smarttesting.utils.sso;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

public class Algorithm {

    static SecretKeySpec secretKeySpec;

    static byte[] hexToByte(String s) throws IOException {
        return MachineKeySection.HexStringToByteArray(s);
    }

    static String byte2hex(byte[] b) {
        return MachineKeySection.ByteArrayToHexString(b, 0);
    }

    private static final void printBytes(String des, byte[] buf) {
        StringBuilder sbuf = new StringBuilder();
        for (byte b : buf) {
            sbuf.append(b).append(",");
        }
        System.out.println(des + sbuf);
    }

    static Cipher getCipher(int mode, String key) throws Exception {

        String cipherAlgorithm = Arguments.getTransformation();
        String algorithm = Arguments.getAlgorithm();
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        byte[] keyBytes = hexToByte(key);
        if (Arguments.getKeySize() == 256) {
            synchronized (Algorithm.class) {
                if (secretKeySpec == null) {
                    secretKeySpec = new SecretKeySpec(keyBytes, algorithm);
                }
                cipher.init(mode, secretKeySpec, new IvParameterSpec(new byte[16]));
            }
        } else {
            synchronized (Algorithm.class) {
                // mac下某版本的secretKey的encoded加解密必须一样，做成单例, 且不能随机
                if (secretKeySpec == null) {
//                    KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
//                    kgen.init(128, new SecureRandom(keyBytes));
//                    SecretKey secretKey = kgen.generateKey();
                    SecretKeySpec secretKeySpec1 = new SecretKeySpec(keyBytes, algorithm);
                    byte[] s = secretKeySpec1.getEncoded();
                    byte[] dst2 = new byte[16];
                    System.arraycopy(s, 0, dst2, 0, 16);
                    secretKeySpec = new SecretKeySpec(dst2, algorithm);
                }
            }
            cipher.init(mode, secretKeySpec);
        }
        return cipher;

    }

    static byte[] decrypt(String str, String key) throws Exception {
        if (null == str || str.trim().length() < 1) {
            return null;
        }

        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key);

        byte[] encrypted1 = hexToByte(str);
        byte[] original = cipher.doFinal(encrypted1);

        return original;
    }


    static String encrypt(byte[] str, String key) throws Exception {
        if (null == str || str.length < 1) {
            return null;
        }
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted1 = str;
        byte[] original = cipher.doFinal(encrypted1);

        String originalString = byte2hex(original);
        return originalString;

    }

    static String encrypt(String str, String key) throws Exception {
        if (null == str || str.trim().length() < 1) {
            return null;
        }

        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key);

        byte[] encrypted1 = str.getBytes(Arguments.getEncoding());
        byte[] original = cipher.doFinal(encrypted1);

        String originalString = byte2hex(original);
        return originalString;

    }

    static byte[] decryptBytes(String str, String key) throws Exception {
        if (null == str || str.trim().length() < 1) {
            return null;
        }

        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key);

        byte[] encrypted1 = hexToByte(str);
        byte[] original = cipher.doFinal(encrypted1);

        return original;
    }

    static byte[] encryptByte(String str, String key) throws Exception {
        if (null == str || str.trim().length() < 1) {
            return null;
        }

        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key);

        byte[] encrypted1 = str.getBytes(Arguments.getEncoding());
        byte[] original = cipher.doFinal(encrypted1);

        return original;

    }

    static byte[] readUtf16le(byte[] ticketBytes, int start) {
        int end = checkUtf16leEnd(ticketBytes, start);
        if (end < 0) {
            return new byte[0];
        } else {
            int len = end - start;
            byte[] desc = new byte[len];
            System.arraycopy(ticketBytes, start, desc, 0, len);
            return desc;
        }
    }

    static int checkUtf16leEnd(byte[] ticketBytes, int start) {
        int end = ticketBytes.length; // 默认到末尾

        for (int i = start; i < ticketBytes.length - 1; i += 2) {
            byte i1 = ticketBytes[i];
            byte i2 = ticketBytes[i + 1];
            if (i1 == 0 && i2 == 0) {
                end = i;
                break;
            }
        }
        return end;
    }

    static Ticket parseTicket(byte[] ticketBytes) throws Exception {

        Ticket ticket = null;
        int pos = 8;

        int version = (int) ticketBytes[pos];
        pos++;

        byte[] usernames = readUtf16le(ticketBytes, pos);
        pos += usernames.length + 2;

        byte[] createTimeBytes = new byte[8];
        System.arraycopy(ticketBytes, pos, createTimeBytes, 0, createTimeBytes.length);
        pos += createTimeBytes.length;

        int isPersist = (int) ticketBytes[pos];
        pos++;

        byte[] expireTimeBytes = new byte[8];
        System.arraycopy(ticketBytes, pos, expireTimeBytes, 0, expireTimeBytes.length);
        pos += expireTimeBytes.length;

        byte[] userdatas = readUtf16le(ticketBytes, pos);
        pos += userdatas.length + 2;

        byte[] paths = readUtf16le(ticketBytes, pos);
        pos += paths.length + 2;

        try {

            String username = new String(usernames, Arguments.getEncoding());
            String userData = new String(userdatas, Arguments.getEncoding());
            String appPath = new String(paths, Arguments.getEncoding());

            Date createTime = ByteArrayToDate(createTimeBytes);
            Date expiration = ByteArrayToDate(expireTimeBytes);
            boolean isPersistent = isPersist == 1;
            ticket = new Ticket(username, userData, appPath, createTime, expiration, version, isPersistent);

        } catch (Exception e) {
            throw e;
        }
        return ticket;
    }

    static byte[] longToByteArray(long l) {
        byte[] bArray = new byte[8];
        ByteBuffer bBuffer = ByteBuffer.wrap(bArray);
        bBuffer.order(ByteOrder.LITTLE_ENDIAN);
        LongBuffer lBuffer = bBuffer.asLongBuffer();
        lBuffer.put(0, l);
        return bArray;
    }

    static long byteArrayToLong(byte[] bArray) {
        ByteBuffer bBuffer = ByteBuffer.wrap(bArray);
        bBuffer.order(ByteOrder.LITTLE_ENDIAN);
        LongBuffer lBuffer = bBuffer.asLongBuffer();
        long l = lBuffer.get(0);
        return l;
    }

    static byte[] DateToByteArray(Date date) {
        long longDate = date.getTime();
        longDate *= 10000;
        longDate += 116444736000000000L;
        return longToByteArray(longDate);
    }

    static Date ByteArrayToDate(byte[] bytes) throws Exception {
        if (bytes.length != 8)
            throw new Exception("must be 8 bytes");
        long date = byteArrayToLong(bytes);
        return new Date((date - 116444736000000000L) / 10000);
    }

    public static byte[] makeTicketBlob(byte[] random, byte version, byte[] username, byte[] issueDate, byte persist, byte[] expires, byte[] userdata, byte[] appPath)
            throws Exception {
        if (random.length != 8)
            throw new Exception("random");
        byte[] buffer = null;
        int bufferLength = 8;
        bufferLength += random.length;
        bufferLength += username.length;
        bufferLength += issueDate.length;
        bufferLength += expires.length;
        if (userdata != null)
            bufferLength += userdata.length;
        bufferLength += appPath.length;
        buffer = new byte[bufferLength];
        int pos = 0;
        System.arraycopy(random, 0, buffer, pos, random.length);
        pos += random.length;
        buffer[pos] = version;
        pos++;
        System.arraycopy(username, 0, buffer, pos, username.length);
        pos += username.length;
        buffer[pos] = 0;
        pos++;
        buffer[pos] = 0;
        pos++;
        System.arraycopy(issueDate, 0, buffer, pos, issueDate.length);
        pos += issueDate.length;
        buffer[pos] = persist;
        pos++;
        System.arraycopy(expires, 0, buffer, pos, expires.length);
        pos += expires.length;
        if (userdata != null) {
            System.arraycopy(userdata, 0, buffer, pos, userdata.length);
            pos += userdata.length;
        }
        buffer[pos] = 0;
        pos++;
        buffer[pos] = 0;
        pos++;
        System.arraycopy(appPath, 0, buffer, pos, appPath.length);
        pos += appPath.length;
        buffer[pos] = 0;

        return buffer;
    }

    public static byte[] md5HashForData(byte[] buf, int start, int length, String validationKeyString) throws NoSuchAlgorithmException, IOException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.getDigestLength();
        byte[] validationKeyBytes = Algorithm.hexToByte(validationKeyString);
        int num = length + validationKeyBytes.length; //

        byte[] dst = new byte[num];
        System.arraycopy(buf, start, dst, 0, length);

        System.arraycopy(validationKeyBytes, 0, dst, length, validationKeyBytes.length);
        md.update(dst);

        return md.digest();
    }

    public static boolean isSignatureVerified(byte[] decryptedData, String validationKeyString) throws IOException, NoSuchAlgorithmException {
        byte[] signatureeBytes = md5HashForData(decryptedData, 0, decryptedData.length - 20, validationKeyString);
        if (signatureeBytes.length < 20) {
            byte[] dst = new byte[20];
            System.arraycopy(signatureeBytes, 0, dst, 0, signatureeBytes.length);
            signatureeBytes = dst;
        }

        for (int i = 0; i < 20; i++) {
            if (signatureeBytes[i] != decryptedData[(decryptedData.length - 20) + i])
                return false;
        }
        return true;
    }

}