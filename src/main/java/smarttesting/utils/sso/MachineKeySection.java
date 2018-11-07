package smarttesting.utils.sso;

public class MachineKeySection {

    private static byte[] s_ahexval;
    private static char[] s_acharval;

    static String ByteArrayToHexString(byte[] buf, int iLen) {
        char[] chArray = s_acharval;
        if (chArray == null) {
            chArray = new char[0x10];
            int length = chArray.length;
            while (--length >= 0) {
                if (length < 10) {
                    chArray[length] = (char) (0x30 + length);
                } else {
                    chArray[length] = (char) (0x41 + (length - 10));
                }
            }
            s_acharval = chArray;
        }
        if (buf == null) {
            return null;
        }
        if (iLen == 0) {
            iLen = buf.length;
        }
        char[] chArray2 = new char[iLen * 2];

        char[] chRef2 = chArray;
        char[] chPtr = chArray2;
        byte[] numPtr = buf;
        for (; --iLen >= 0;) {
            int k = iLen * 2;
            chPtr[k] = chRef2[(numPtr[iLen] & 240) >> 4];
            chPtr[k + 1] = chRef2[numPtr[iLen] & 15];
        }

        return new String(chArray2);
    }

    static byte[] HexStringToByteArray(String str) {
        if ((str.length() & 1) == 1) {
            return null;
        }
        byte[] buffer = s_ahexval;
        if (buffer == null) {
            buffer = new byte[0x67];
            int index = buffer.length;
            while (--index >= 0) {
                if ((0x30 <= index) && (index <= 0x39)) {
                    buffer[index] = (byte) (index - 0x30);
                } else {
                    if ((0x61 <= index) && (index <= 0x66)) {
                        buffer[index] = (byte) ((index - 0x61) + 10);
                        continue;
                    }
                    if ((0x41 <= index) && (index <= 70)) {
                        buffer[index] = (byte) ((index - 0x41) + 10);
                    }
                }
            }
            s_ahexval = buffer;
        }
        byte[] buffer2 = new byte[str.length() / 2];
        int num2 = 0;
        int num3 = 0;
        int length = buffer2.length;
        char[] charStr = str.toCharArray();
        while (--length >= 0) {
            int num5;
            int num6;
            try {
                num5 = buffer[charStr[num2++]];
            } catch (Exception e) {
                num5 = 0;
                return null;
            }
            try {
                num6 = buffer[charStr[num2++]];
            } catch (Exception e) {
                num6 = 0;
                return null;
            }
            buffer2[num3++] = (byte) ((num5 << 4) + num6);
        }
        return buffer2;
    }

}
