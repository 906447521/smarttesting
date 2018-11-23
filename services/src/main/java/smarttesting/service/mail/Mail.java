package smarttesting.service.mail;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Pattern;

final class Mail {

    public final static boolean SUCCESSFUL = true;

    public final static boolean FAILED = false;

    private final static int PORT     = 25;
    private final static int RETRY    = 3;
    private final static int INTERVAL = 1000;
    private final static int TIMEOUT  = 10000;

    private final static String  BOUNDARY;
    private final static String  CHARSET;
    private final static Pattern PATTERN;

    private static InitialDirContext dirContext;

    private final ArrayList<MailLog> logManager;

    private boolean isEsmtp;

    private String smtp;
    private String user;
    private String password;
    private String sender;
    private String senderAddress;

    static {
        BOUNDARY = "Boundary-=_hMbeqwnGNoWeLsRMeKTIPeofyStu";
        CHARSET = "UTF-8";// Charset.defaultCharset().displayName();
        PATTERN = Pattern.compile(".+@[^.@]+(\\.[^.@]+)+$");

        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

        try {
            dirContext = new InitialDirContext(hashtable);
        } catch (NamingException e) {
        }
    }

    private Mail(String from) {
        if (from == null) {
            throw new IllegalArgumentException("参数from不能为null。");
        }

        int leftSign = (from = from.trim()).charAt(from.length() - 1) == '>' ? from.lastIndexOf('<') : -1;

        senderAddress = leftSign > -1 ? from.substring(leftSign + 1, from.length() - 1).trim() : from;

        if (!PATTERN.matcher(senderAddress).find()) {
            throw new IllegalArgumentException("参数from不正确。");
        }

        sender = leftSign > -1 ? from.substring(0, leftSign).trim() : null;
        logManager = new ArrayList<MailLog>();
        isEsmtp = false;

        if (sender != null) {
            if (sender.length() == 0) {
                sender = null;
            } else if (sender.charAt(0) == '"' && sender.charAt(sender.length() - 1) == '"') {
                sender = sender.substring(1, sender.length() - 1).trim();
            }
        }
    }

    private Mail(String address, String from, String user, String password) {
        this(from);

        isEsmtp = true;
        this.smtp = address;
        this.user = SMTPBase64.encode(user.getBytes());
        this.password = SMTPBase64.encode(password.getBytes());
    }

    public static Mail createSmtpMailSender(String from) throws IllegalArgumentException {
        return new Mail(from);
    }

    public static Mail createESmtpMailSender(String smtp, String from, String user, String password)
            throws IllegalArgumentException {
        return new Mail(smtp, from, user, password);
    }

    public boolean sendMail(String to, String subject, String content, File[] attachments, boolean isHtml,
                            boolean isUrgent) throws IllegalArgumentException {
        if (to == null) {
            throw new IllegalArgumentException("参数to不能为null。");
        }

        int leftSign = (to = to.trim()).charAt(to.length() - 1) == '>' ? to.lastIndexOf('<') : -1;

        String addresseeAddress = leftSign > -1 ? to.substring(leftSign + 1, to.length() - 1).trim() : to;// 收件人的E-Mail地址

        if (!PATTERN.matcher(addresseeAddress).find()) {
            throw new IllegalArgumentException("参数to不正确。");
        }

        String addressee = leftSign > -1 ? to.substring(0, leftSign).trim() : null;// 收件人名字
        boolean needBoundary = attachments != null && attachments.length > 0;

        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        byte[] data;

        try {
            if (addressee != null) {
                if (addressee.length() == 0) {
                    addressee = null;
                } else if (addressee.charAt(0) == '"' && addressee.charAt(addressee.length() - 1) == '"') {
                    addressee = addressee.substring(1, addressee.length() - 1).trim();
                }
            }

            if (isEsmtp) {
                for (int k = 1; ; k++) {
                    try {
                        log("连接: 主机:\"" + smtp + "\" 端口:\"" + PORT + "\"");
                        socket = new Socket(smtp, PORT);
                        break;
                    } catch (IOException e) {
                        log("错误: 连接失败" + k + "次");

                        if (k == RETRY) {
                            return FAILED;
                        }

                        try {
                            Thread.sleep(INTERVAL);
                        } catch (InterruptedException ie) {
                        }
                    }
                }

                in = socket.getInputStream();
                out = socket.getOutputStream();

                if (response(in) != 220) {
                    return FAILED;
                }
            } else {
                log("状态: 创建邮件接收服务器列表");
                String[] address = parseDomain(parseUrl(addresseeAddress));

                if (address == null) {
                    return FAILED;
                }

                for (int k = 0; k < address.length; k++) {
                    try {
                        log("连接: 主机:\"" + address[k] + "\" 端口:\"" + PORT + "\"");

                        socket = new Socket(address[k], PORT);

                        in = socket.getInputStream();
                        out = socket.getOutputStream();

                        if (response(in) != 220) {
                            return FAILED;
                        }

                        break;
                    } catch (IOException e) {
                        log("错误: 连接失败");
                    }
                }
            }

            if (in == null || out == null) {
                return FAILED;
            }

            socket.setSoTimeout(TIMEOUT);

            sendString("HELO " + parseUrl(senderAddress), out);
            sendNewline(out);

            if (response(in) != 250) {
                return FAILED;
            }

            if (isEsmtp) {
                sendString("AUTH LOGIN", out);
                sendNewline(out);

                if (response(in) != 334) {
                    return FAILED;
                }

                sendString(user, out);
                sendNewline(out);

                if (response(in) != 334) {
                    return FAILED;
                }

                sendString(password, out);
                sendNewline(out);

                if (response(in) != 235) {
                    return FAILED;
                }
            }

            sendString("MAIL FROM: <" + senderAddress + ">", out);
            sendNewline(out);

            if (response(in) != 250) {
                return FAILED;
            }

            sendString("RCPT TO: <" + addresseeAddress + ">", out);
            sendNewline(out);

            if (response(in) != 250) {
                return FAILED;
            }

            sendString("DATA", out);
            sendNewline(out);

            if (response(in) != 354) {
                return FAILED;
            }

            sendString("From: "
                    + (sender == null ? senderAddress : getBase64String(sender) + " <" + senderAddress + ">"), out);
            sendNewline(out);
            sendString("To: "
                    + (addressee == null ? addresseeAddress : getBase64String(addressee) + " <" + addresseeAddress
                    + ">"), out);
            sendNewline(out);
            sendString("Subject: " + getBase64String(subject), out);
            sendNewline(out);
            sendString("Date: " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z (z)", Locale.US).format(new Date()),
                    out);
            sendNewline(out);
            sendString("MIME-Version: 1.0", out);
            sendNewline(out);

            if (needBoundary) {
                sendString("Content-Type: multipart/mixed; BOUNDARY=\"" + BOUNDARY + "\"", out);
                sendNewline(out);
            } else {
                if (isHtml) {
                    sendString("Content-Type: text/html; charset=\"" + CHARSET + "\"", out);
                    sendNewline(out);
                } else {
                    sendString("Content-Type: text/plain; charset=\"" + CHARSET + "\"", out);
                    sendNewline(out);
                }
            }

            sendString("Content-Transfer-Encoding: base64", out);
            sendNewline(out);

            if (isUrgent) {
                sendString("X-Priority: 1", out);
                sendNewline(out);
            } else {
                sendString("X-Priority: 3", out);
                sendNewline(out);
            }

            sendString("X-Mailer: SC.DNS Mail[Copyright(C) 2011 sc.dns]", out);
            sendNewline(out);

            log("发送: ");
            sendNewline(out);

            if (needBoundary) {
                sendString("--" + BOUNDARY, out);
                sendNewline(out);

                if (isHtml) {
                    sendString("Content-Type: text/html; charset=\"" + CHARSET + "\"", out);
                    sendNewline(out);
                } else {
                    sendString("Content-Type: text/plain; charset=\"" + CHARSET + "\"", out);
                    sendNewline(out);
                }

                sendString("Content-Transfer-Encoding: base64", out);
                sendNewline(out);

                log("发送: ");
                sendNewline(out);
            }

            data = (content != null ? content : "").getBytes(CHARSET);

            for (int k = 0; k < data.length; k += 54) {
                sendString(SMTPBase64.encode(data, k, Math.min(data.length - k, 54)), out);
                sendNewline(out);
            }

            if (needBoundary) {
                RandomAccessFile attachment = null;
                int fileIndex = 0;
                String fileName;
                int k;
                data = new byte[54];

                try {
                    for (; fileIndex < attachments.length; fileIndex++) {
                        fileName = attachments[fileIndex].getName();
                        attachment = new RandomAccessFile(attachments[fileIndex], "r");

                        sendString("--" + BOUNDARY, out);
                        sendNewline(out);
                        sendString(
                                "Content-Type: "
                                        + SMTPMimeType.getMimeType(fileName.indexOf(".") == -1 ? "*" : fileName
                                        .substring(fileName.lastIndexOf(".") + 1)) + "; name=\""
                                        + (fileName = getBase64String(fileName)) + "\"", out);
                        sendNewline(out);
                        sendString("Content-Transfer-Encoding: base64", out);
                        sendNewline(out);
                        sendString("Content-Disposition: attachment; filename=\"" + fileName + "\"", out);
                        sendNewline(out);

                        log("发送: ");
                        sendNewline(out);

                        do {
                            k = attachment.read(data, 0, 54);

                            if (k == -1) {
                                break;
                            }

                            sendString(SMTPBase64.encode(data, 0, k), out);
                            sendNewline(out);
                        } while (k == 54);
                    }
                } catch (FileNotFoundException e) {
                    log("错误: 附件\"" + attachments[fileIndex].getAbsolutePath() + "\"不存在");
                    return FAILED;
                } catch (IOException e) {
                    log("错误: 无法读取附件\"" + attachments[fileIndex].getAbsolutePath() + "\"");
                    return FAILED;
                } finally {
                    if (attachment != null) {
                        try {
                            attachment.close();
                        } catch (IOException e) {
                        }
                    }
                }

                sendString("--" + BOUNDARY + "--", out);
                sendNewline(out);
            }

            sendString(".", out);
            sendNewline(out);

            if (response(in) != 250) {
                return FAILED;
            }

            sendString("QUIT", out);
            sendNewline(out);

            if (response(in) != 221) {
                return FAILED;
            }

            return SUCCESSFUL;
        } catch (SocketTimeoutException e) {
            log("错误: 连接超时");
            return FAILED;
        } catch (IOException e) {
            log("错误: 连接出错");
            return FAILED;
        } catch (Exception e) {
            log("错误: " + e.toString());
            return FAILED;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public boolean[] sendMail(String[] to, String subject, String content, File[] attachments, boolean isHtml,
                              boolean isUrgent) throws IllegalArgumentException {
        boolean[] task = new boolean[to.length];

        for (int k = 0; k < task.length; k++) {
            task[k] = sendMail(to[k], subject, content, attachments, isHtml, isUrgent);
        }

        return task;
    }

    public boolean sendTextMail(String to, String subject, String content) throws IllegalArgumentException {
        return sendMail(to, subject, content, null, false, false);
    }

    public boolean sendHtmlMail(String to, String subject, String content) throws IllegalArgumentException {
        return sendMail(to, subject, content, null, true, false);
    }

    public boolean[] sendTextMail(String[] to, String subject, String content) throws IllegalArgumentException {
        return sendMail(to, subject, content, null, false, false);
    }

    public boolean[] sendHtmlMail(String[] to, String subject, String content) throws IllegalArgumentException {
        return sendMail(to, subject, content, null, true, false);
    }

    public boolean sendTextMail(String to, String subject, String content, File[] attachments)
            throws IllegalArgumentException {
        return sendMail(to, subject, content, attachments, false, false);
    }

    public boolean sendHtmlMail(String to, String subject, String content, File[] attachments)
            throws IllegalArgumentException {
        return sendMail(to, subject, content, attachments, true, false);
    }

    public boolean[] sendTextMail(String[] to, String subject, String content, File[] attachments)
            throws IllegalArgumentException {
        return sendMail(to, subject, content, attachments, false, false);
    }

    public boolean[] sendHtmlMail(String[] to, String subject, String content, File[] attachments)
            throws IllegalArgumentException {
        return sendMail(to, subject, content, attachments, true, false);
    }

    public void addLogManager(MailLog manager) {
        logManager.add(manager);
    }

    public void removeLogManager(MailLog manager) {
        logManager.remove(manager);
    }

    private String[] parseDomain(String url) {
        try {
            NamingEnumeration records = dirContext.getAttributes(url, new String[]{"mx"}).getAll();

            String[] address;
            String[] tmpMx;
            MX[] tmpMxArray;
            MX tmp;

            if (records.hasMore()) {
                url = records.next().toString();
                url = url.substring(url.indexOf(": ") + 2);
                address = url.split(",");
                tmpMxArray = new MX[address.length];

                for (int k = 0; k < address.length; k++) {
                    tmpMx = address[k].trim().split(" ");
                    tmpMxArray[k] = new MX(Integer.parseInt(tmpMx[0]), tmpMx[1]);
                }

                for (int n = 1; n < tmpMxArray.length; n++) {
                    for (int m = n; m > 0; m--) {
                        if (tmpMxArray[m - 1].pri > tmpMxArray[m].pri) {
                            tmp = tmpMxArray[m - 1];
                            tmpMxArray[m - 1] = tmpMxArray[m];
                            tmpMxArray[m] = tmp;
                        }
                    }
                }

                for (int k = 0; k < tmpMxArray.length; k++) {
                    address[k] = tmpMxArray[k].address;
                }

                return address;
            }// 分析mx记录

            records = dirContext.getAttributes(url, new String[]{"a"}).getAll();

            if (records.hasMore()) {
                url = records.next().toString();
                url = url.substring(url.indexOf(": ") + 2).replace(" ", "");
                address = url.split(",");

                return address;
            }// 分析a记录

            return new String[]{url};
        } catch (NamingException e) {
            log("错误: 域名\"" + url + "\"无法解析");
            return null;
        }
    }

    private int response(InputStream in) throws IOException {
        byte[] buffer = new byte[1024];
        int k = in.read(buffer);

        if (k == -1) {
            return -1;
        }

        String response = new String(buffer, 0, k).trim();
        log("响应: " + response);
        return Integer.parseInt(response.substring(0, 3));
    }

    private void sendString(String str, OutputStream out) throws IOException {
        log("发送: " + str);

        if (str == null) {
            str = "";
        }

        out.write(str.getBytes());
        out.flush();
    }

    private void log(String info) {
        for (int n = 0, m = logManager.size(); n < m; n++) {
            logManager.get(n).info(info);
        }
    }

    private static void sendNewline(OutputStream out) throws IOException {
        out.write('\r');
        out.write('\n');
        out.flush();
    }

    private static String getBase64String(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }

        StringBuffer tmpStr = new StringBuffer();
        byte[] bytes = new byte[0];
        try {
            bytes = str.getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
        }

        for (int k = 0; k < bytes.length; ) {
            if (k != 0) {
                tmpStr.append(' ');
            }

            tmpStr.append("=?");
            tmpStr.append(CHARSET);
            tmpStr.append("?B?");
            tmpStr.append(SMTPBase64.encode(bytes, k, Math.min(bytes.length - k, 30)));
            tmpStr.append("?=");

            k += 30;

            if (k < bytes.length) {
                tmpStr.append('\r');
                tmpStr.append('\n');
            }
        }

        return tmpStr.toString();
    }

    private static String parseUrl(String address) {
        return address.substring(address.lastIndexOf('@') + 1);
    }

    private class MX {
        final int    pri;
        final String address;

        MX(int pri, String host) {
            this.pri = pri;
            this.address = host;
        }
    }
}