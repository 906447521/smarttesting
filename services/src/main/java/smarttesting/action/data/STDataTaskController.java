package smarttesting.action.data;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smarttesting.data.model.STTask;
import smarttesting.service.*;
import smarttesting.service.model.DataResult;
import smarttesting.service.model.ServiceResultFail;
import smarttesting.utils.Query;

import javax.annotation.Resource;

/**
 * @author
 */
@RequestMapping(value = "data")
@Controller
public class STDataTaskController {

    @Resource
    private STUserService      zdUserService;
    @Resource
    private STProjectService   zdProjectService;
    @Resource
    private STInterfaceService zdInterfaceService;
    @Resource
    private STCaseService      zdCaseService;
    @Resource
    private STTaskService      zdTaskService;

    @ResponseBody
    @RequestMapping(value = "/task/run.json")
    public DataResult data_task_run(@RequestParam(value = "zdCaseIds[]") Long[] zdCaseIds) {
        return DataResult.successResult(zdCaseService.run(zdCaseIds));
    }

    @ResponseBody
    @RequestMapping(value = "/task/autorun.json")
    public DataResult data_task_autorun(STTask zdTask) {
        if (zdTask.getRun()) {
            return DataResult.successResult(zdTaskService.startTask(zdTask));
        } else {
            return DataResult.successResult(zdTaskService.stopTask(zdTask));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/task/single.json")
    public DataResult data_task_single(Integer id) {
        return DataResult.successResult(
                zdTaskService.find(
                        new Query().with("id", id)).singleResult());

    }

    @ResponseBody
    @RequestMapping(value = "/task/list.json")
    public DataResult data_task_list(@RequestParam(required = false) Integer projectId, Integer pageIndex, Integer pageSize) {
        return DataResult.successResult(
                zdTaskService.find(
                        new Query()
                                .with("projectId", projectId)
                                .withPager(pageIndex, pageSize)
                                .withOrder("id", "desc")));
    }

    @ResponseBody
    @RequestMapping(value = "/task/add.json")
    public DataResult data_task_add(STTask zdTask) {
        if(StringUtils.isEmpty(zdTask.getName())) {
            throw new ServiceResultFail("名称不能为空！");
        }
        return DataResult.successResult(zdTaskService.add(zdTask));
    }

    @ResponseBody
    @RequestMapping(value = "/task/edit.json")
    public DataResult data_task_edit(STTask zdTask) {
        return DataResult.successResult(zdTaskService.edit(zdTask));
    }


    @Transactional
    @ResponseBody
    @RequestMapping(value = "/task/delete.json")
    public DataResult data_task_delete(@RequestParam(value = "zdTaskIds[]") Long[] zdTaskIds) {
        return DataResult.successResult(zdTaskService.delete(zdTaskIds));
    }

    @ResponseBody
    @RequestMapping(value = "/task_result/detail.json")
    public DataResult data_task_detail(String resultId) {
        return DataResult.successResult(zdTaskService.findZDTaskSingleResult(resultId));
    }


}
