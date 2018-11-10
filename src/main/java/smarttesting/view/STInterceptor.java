package smarttesting.view;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import smarttesting.utils.RequestContext;
import smarttesting.utils.sso.Ticket;
import smarttesting.utils.sso.TicketUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author
 */
public class STInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.preHandle(request, response, handler);

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String url = request.getRequestURI();
        if (url.endsWith("login.json")) {
            return true;
        }

        if (url.endsWith("logout.json")) {
        }

        String zdPIN = null;
        String zdGROUP = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (RequestContext.REQUEST_USER_PIN.equals(cookie.getName())) {
                    try {
                        Ticket ticket = TicketUtil.decrypt(cookie.getValue(), false);
                        zdPIN = ticket.getUsername();
                        zdGROUP = ticket.getUserData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        if (zdPIN == null) {
            try {
                response.sendRedirect("/login.html");
            } catch (IOException e) {
            }
            return false;
        }

        RequestContext.initContext(request, response);
        request.setAttribute("Request", request);
        request.setAttribute("zdPIN", zdPIN);
        request.setAttribute("zdGROUP", zdGROUP);
        request.setAttribute("ContextPath", request.getContextPath());
        request.setAttribute("BasePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
        request.setAttribute("ServerPath", request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        RequestContext.remove();

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
