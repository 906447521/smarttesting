package smarttesting.action.data;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smarttesting.data.model.STProject;
import smarttesting.data.model.STProjectUser;
import smarttesting.service.STCaseService;
import smarttesting.service.STInterfaceService;
import smarttesting.service.STProjectService;
import smarttesting.service.STUserService;
import smarttesting.service.model.DataResult;
import smarttesting.service.model.ServiceResultFail;
import smarttesting.utils.Query;
import smarttesting.utils.RequestContext;

import javax.annotation.Resource;

/**
 * @author
 */
@RequestMapping(value = "data")
@Controller
public class STDataProjectController {

    @Resource
    private STUserService      zdUserService;
    @Resource
    private STProjectService   zdProjectService;
    @Resource
    private STInterfaceService zdInterfaceService;
    @Resource
    private STCaseService      zdCaseService;


    @ResponseBody
    @RequestMapping(value = "/project/single.json")
    public DataResult data_project_single(Integer id) {
        return DataResult.successResult(
                zdProjectService.find(
                        new Query().with("id", id)).singleResult());
    }

    @ResponseBody
    @RequestMapping(value = "/project/list_promotion.json")
    public DataResult data_project_list_promotion() {

        String user = RequestContext.getUserPin();
        String group = RequestContext.getUserGroup();

        if ("admin".equals(group)) {
            return DataResult.successResult(
                    zdProjectService.find(
                            new Query()
                                    .withOrder("id", "desc")));
        }

        return DataResult.successResult(
                zdProjectService.findAllPromotion(
                        new Query()
                                .with("userIn", user)
                                .withOrder("id", "desc")));
    }

    @ResponseBody
    @RequestMapping(value = "/project/list.json")
    public DataResult data_project_list(Integer pageIndex, Integer pageSize) {
        return DataResult.successResult(
                zdProjectService.find(
                        new Query()
                                .withPager(pageIndex, pageSize)
                                .withOrder("id", "desc")));
    }

    @ResponseBody
    @RequestMapping(value = "/project/add.json")
    public DataResult data_project_add(STProject zdProject) {
        if (RequestContext.getUserPin() == null) {
            throw new ServiceResultFail("创建者异常！请确认是否登录！");
        }
        zdProject.setCreator(RequestContext.getUserPin());
        return DataResult.successResult(zdProjectService.add(zdProject));
    }

    @ResponseBody
    @RequestMapping(value = "/project/edit.json")
    public DataResult data_project_edit(STProject zdProject) {
        return DataResult.successResult(zdProjectService.edit(zdProject));
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "/project/delete.json")
    public DataResult data_project_delete(@RequestParam(value = "zdProjectIds[]") Long[] zdProjectIds) {
        return DataResult.successResult(zdProjectService.delete(zdProjectIds));
    }

    @ResponseBody
    @RequestMapping(value = "/project/list_member.json")
    public DataResult data_project_list_member(Long projectId) {
        return DataResult.successResult(
                zdProjectService.findMember(
                        new Query().with("projectId", projectId)
                                .withOrder("id", "desc")));
    }

    @ResponseBody
    @RequestMapping(value = "/project/add_member.json")
    public DataResult data_project_add_member(STProjectUser zdProjectUser) {
        return DataResult.successResult(zdProjectService.addMember(zdProjectUser));
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "/project/delete_member.json")
    public DataResult data_project_delete_member(@RequestParam(value = "zdProjectUserIds[]") Long[] zdProjectUserIds) {
        return DataResult.successResult(zdProjectService.deleteMember(zdProjectUserIds));
    }

}
