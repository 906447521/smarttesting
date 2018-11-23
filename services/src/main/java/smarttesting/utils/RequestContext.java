package smarttesting.utils;


import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import smarttesting.utils.uuid.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author
 */
public class RequestContext {

    private HttpServletRequest  request;
    private HttpServletResponse response;

    private String[] ipArray;
    private String   userPin;
    private String   userName;
    private String   userGroup;
    private String   requestId;

    private RequestContext() {

    }

    public static final RequestContext getRequestHolder() {
        RequestContext context = local.get();
        return context;
    }

    public static final HttpServletRequest getRequest() {
        RequestContext context = getRequestHolder();
        if (context == null) {
            return null;
        }
        return (HttpServletRequest) context.request;
    }

    public static final String getServerName() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return (String) request.getServerName();
    }

    public static final HttpServletResponse getResponse() {
        RequestContext context = getRequestHolder();
        if (context == null) {
            return null;
        }
        return context.response;
    }

    public static final String getRequestId() {
        RequestContext context = getRequestHolder();
        if (context == null) {
            return null;
        }
        return context.requestId;
    }

    public static final String getUserPin() {
        RequestContext context = getRequestHolder();
        if (context == null) {
            return null;
        }

        if (context.userPin == null) {
            HttpServletRequest request = context.request;
            if (request != null) {
                context.userPin = (String) request.getAttribute(REQUEST_USER_PIN);
            }

        }
        try {
            if (context.userPin == null) {
                return null;
            }
            return URLDecoder.decode(context.userPin, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final String getUserGroup() {
        RequestContext context = getRequestHolder();
        if (context == null) {
            return null;
        }

        if (context.userGroup == null) {
            HttpServletRequest request = context.request;
            if (request != null) {
                context.userGroup = (String) request.getAttribute(REQUEST_USER_GROUP);
            }

        }
        try {
            if (context.userGroup == null) {
                return null;
            }
            return URLDecoder.decode(context.userGroup, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final String getUserName() {
        RequestContext context = getRequestHolder();
        if (context == null) {
            return null;
        }

        if (context.userName == null) {
            HttpServletRequest request = context.request;
            if (request != null) {
                context.userName = (String) request.getAttribute(REQUEST_USER_NAME);
            }

        }
        return context.userName;
    }

    @Deprecated
    public static final ApplicationContext getApplicationContext() {
        return ContextLoader.getCurrentWebApplicationContext();
    }

    public static final String[] getIpArray() {
        RequestContext context = getRequestHolder();
        if (context == null) {
            return null;
        }
        if (context.ipArray == null) {
            context.ipArray = IP.getIpArray(context.request);
        }
        return context.ipArray;
    }

    public static final String getIp() {
        String[] ipArray = getIpArray();
        if (ipArray != null && ipArray.length > 0) {
            return ipArray[0];
        }
        return null;
    }

    public static final void remove() {
        local.remove();
    }

    public static final void initContext(RequestContext context) {
        local.set(context);
    }

    public static final void initContext(HttpServletRequest request, HttpServletResponse response) {

        RequestContext context = new RequestContext();

        context.request = request;
        context.response = response;

        context.requestId = new UUID().toString32();
        local.set(context);
    }

    private static final ThreadLocal<RequestContext> local              = new ThreadLocal<RequestContext>();
    public static final  String                      REQUEST_USER_NAME  = "zdUNICK";
    public static final  String                      REQUEST_USER_PIN   = "zdPIN";
    public static final  String                      REQUEST_USER_GROUP = "zdGROUP";
}
