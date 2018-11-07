package smarttesting.utils.sso;

public class TicketUtil {

    public static String encrypt(Ticket ticket) throws Exception {

        byte[] random = new byte[8];
        byte version = (byte) ticket.getVersion();
        byte[] name = ticket.getUsername().getBytes(Arguments.getEncoding());
        byte[] issue = Algorithm.DateToByteArray(ticket.getIssueDate());
        byte persist = (byte) 0;
        byte[] expires = Algorithm.DateToByteArray(ticket.getExpires());
        byte[] data = ticket.getUserData().getBytes(Arguments.getEncoding());
        byte[] path = ticket.getAppPath().getBytes(Arguments.getEncoding());

        for (int i = 0; i < random.length; i++) {
            random[i] = (byte) (Math.random() * 100);
        }

        byte[] buf = Algorithm.makeTicketBlob(random, version, name, issue, persist, expires, data, path);
        byte[] signature = Algorithm.md5HashForData(buf, 0, buf.length, Arguments.getValidationKey());

        if (signature.length < 20) {
            byte[] dst3 = new byte[20];
            System.arraycopy(signature, 0, dst3, 0, signature.length);
            signature = dst3;
        }

        byte[] dst2 = new byte[buf.length + signature.length];

        System.arraycopy(buf, 0, dst2, 0, buf.length);
        System.arraycopy(signature, 0, dst2, buf.length, signature.length);

        buf = dst2;

        return Algorithm.encrypt(buf, Arguments.getDecryptionKey());
    }

    public static byte[] decrypt(String ticket) throws Exception {
        byte[] buf = Algorithm.decrypt(ticket, Arguments.getDecryptionKey());
        return buf;
    }

    public static Ticket decrypt(String ticket, boolean signature) throws Exception {
        byte[] buf = decrypt(ticket);
        if (signature) {
            if (!isSignatureVerified(buf)) {
                return null;
            }
        }
        return Algorithm.parseTicket(buf);
    }

    static boolean isSignatureVerified(byte[] buf) throws Exception {
        return Algorithm.isSignatureVerified(buf, Arguments.getValidationKey());
    }

    public static boolean isSignatureVerified(String ticket) throws Exception {
        byte[] buf = Algorithm.decrypt(ticket, Arguments.getDecryptionKey());
        return isSignatureVerified(buf);
    }

    private static final void printBytes(String des, byte[] buf) {
        StringBuilder sbuf = new StringBuilder();
        for (byte b : buf) {
            sbuf.append(b).append(",");
        }
        System.out.println(des + sbuf);
    }

    private static void testSignature() throws Exception {
        Ticket ticket = null;
        String ticketString = null;
        long s = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            ticket = new Ticket("老大", "admin");
            ticketString = encrypt(ticket);
        }
        System.out.println(System.currentTimeMillis() - s);
        System.out.println("生成串：" + ticketString);
        System.out.println("验签是否成功： " + isSignatureVerified(ticketString));

        long s2 = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            ticket = decrypt(ticketString, true);
        }
        System.out.println(System.currentTimeMillis() - s2);

        System.out.println("解密串：" + ticket);
        printBytes("生成之后：", Algorithm.decrypt(ticketString, Arguments.getDecryptionKey()));
    }

    public static void main(String[] args) throws Exception {

        testSignature();
    }

}
