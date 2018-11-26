package smarttesting.action.data;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smarttesting.data.model.STUser;
import smarttesting.service.*;
import smarttesting.service.model.DataResult;
import smarttesting.service.model.ServiceResultFail;
import smarttesting.utils.MD5;
import smarttesting.utils.Query;
import smarttesting.utils.RequestContext;
import smarttesting.utils.sso.Ticket;
import smarttesting.utils.sso.TicketUtil;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author
 */
@RequestMapping(value = "data")
@Controller
public class STDataLoginController {

    @Resource
    private STUserService      zdUserService;
    @Resource
    private STProjectService   zdProjectService;
    @Resource
    private STInterfaceService zdInterfaceService;
    @Resource
    private STCaseService      zdCaseService;
    @Resource
    private STPluginService    zdPluginService;
    @ResponseBody
    @RequestMapping(value = "/logout.json")
    public void data_logout(HttpServletResponse response) {
        Cookie ticketCookie = new Cookie("zdPIN", "");
        ticketCookie.setMaxAge(-1);
        ticketCookie.setPath("/");
        response.addCookie(ticketCookie);
        try {
            response.sendRedirect("/login.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @ResponseBody
    @RequestMapping(value = "/login.json")
    public DataResult data_login(HttpServletResponse response, STUser zdUser) {

        if ((zdUser.getName() == null || "".equals(zdUser.getName().trim()))
                ) {
            throw new ServiceResultFail("账号不正确！");
        }

        if ((zdUser.getPwd() == null || "".equals(zdUser.getPwd().trim()))
                ) {
            throw new ServiceResultFail("账号不正确！");
        }

        if ((zdUser.getPwd() == null || "".equals(zdUser.getPwd().trim()) || zdUser.getPwd().length() < 6)) {
            throw new ServiceResultFail("密码不正确！密码需要大于六位！");
        }
        if (zdUser.getGroup() == null) {
            zdUser.setGroup("zduser");
        }

        Query query = new Query();
        query.put("pwd", MD5.encrypt(zdUser.getPwd()));
        query.put("name", zdUser.getName());
        STUser zdUser1 = zdUserService.find(query).singleResult();
        if (zdUser1 == null) {
            throw new ServiceResultFail("账号密码不匹配！");
        }

        try {
            Ticket ticket = new Ticket(zdUser1.getName(), zdUser1.getGroup());
            Cookie ticketCookie = new Cookie(RequestContext.REQUEST_USER_PIN, TicketUtil.encrypt(ticket));
            System.out.println("login => " + ticket);
            ticketCookie.setMaxAge(999999999);
            ticketCookie.setPath("/");
            response.addCookie(ticketCookie);
        } catch (Exception e) {
            throw new ServiceResultFail(e.getMessage());
        }


        return DataResult.successResult(zdUser1);

    }

}
