package smarttesting.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 */
public class IP {

    public static final String[] getIpArray(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String[] ipArray = null;
        try {
            String ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (!StringUtils.isEmpty(ip)) {
                ipArray = ip.split(",");
                for (int i = 0; i < ipArray.length; i++) {
                    ipArray[i] = ipArray[i].trim();
                }
            }
        } catch (Throwable e) {
            // ignore
        }
        return ipArray;
    }

    public static final String getIp(HttpServletRequest request) {
        String[] ipArray = getIpArray(request);
        if (ipArray != null && ipArray.length > 0) {
            return ipArray[0];
        }
        return null;
    }


    public static String toIpString(long ipAddress) {

        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf((ipAddress >>> 24)));
        sb.append(".");
        sb.append(String.valueOf((ipAddress & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ipAddress & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf((ipAddress & 0x000000FF)));

        return sb.toString();

    }

    public static long toIpLong(String ipAddress) {

        long[] ip = new long[4];
        int position1 = ipAddress.indexOf(".");
        int position2 = ipAddress.indexOf(".", position1 + 1);
        int position3 = ipAddress.indexOf(".", position2 + 1);

        ip[0] = Long.parseLong(ipAddress.substring(0, position1));
        ip[1] = Long.parseLong(ipAddress.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(ipAddress.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(ipAddress.substring(position3 + 1));

        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];

    }

}
