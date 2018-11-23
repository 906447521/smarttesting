package smarttesting.action.data;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smarttesting.data.model.STUser;
import smarttesting.service.*;
import smarttesting.service.model.DataResult;
import smarttesting.service.model.ServiceResultFail;
import smarttesting.utils.MD5;
import smarttesting.utils.Query;

import javax.annotation.Resource;

/**
 * @author
 */
@RequestMapping(value = "data")
@Controller
public class STDataUserController {

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
    @RequestMapping(value = "/user/single.json")
    public DataResult data_user_single(Integer id) {
        return DataResult.successResult(
                zdUserService.find(
                        new Query().with("id", id)).singleResult());

    }

    @ResponseBody
    @RequestMapping(value = "/user/list.json")
    public DataResult data_user_list(@RequestParam(required = false) Integer pageIndex, Integer pageSize) {
        return DataResult.successResult(
                zdUserService.find(
                        new Query()
                                .withPager(pageIndex, pageSize)
                                .withOrder("id", "desc")));
    }

    @ResponseBody
    @RequestMapping(value = "/user/edit.json")
    public DataResult data_user_edit(STUser zdUser) {
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
        zdUser.setPwd(MD5.encrypt(zdUser.getPwd()));
        return DataResult.successResult(zdUserService.edit(zdUser));
    }

    @ResponseBody
    @RequestMapping(value = "/user/add.json")
    public DataResult data_user_add(STUser zdUser) {
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
        zdUser.setPwd(MD5.encrypt(zdUser.getPwd()));
        return DataResult.successResult(zdUserService.add(zdUser));
    }


    @Transactional
    @ResponseBody
    @RequestMapping(value = "/user/delete.json")
    public DataResult data_user_delete(@RequestParam(value = "zdUserIds[]") Long[] zdUserIds) {
        return DataResult.successResult(zdUserService.delete(zdUserIds));
    }


}
