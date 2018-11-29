package smarttesting.action.data;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smarttesting.data.model.STCase;
import smarttesting.service.STCaseService;
import smarttesting.service.STInterfaceService;
import smarttesting.service.STProjectService;
import smarttesting.service.STUserService;
import smarttesting.service.model.DataResult;
import smarttesting.service.model.ServiceResultFail;
import smarttesting.utils.Query;

import javax.annotation.Resource;

/**
 * @author
 */
@RequestMapping(value = "data")
@Controller
public class STDataCaseController {

    @Resource
    private STUserService      zdUserService;
    @Resource
    private STProjectService   zdProjectService;
    @Resource
    private STInterfaceService zdInterfaceService;
    @Resource
    private STCaseService      zdCaseService;

    @ResponseBody
    @RequestMapping(value = "/case/run.json")
    public DataResult data_case_run(@RequestParam(value = "zdCaseIds[]") Long[] zdCaseIds) {
        return DataResult.successResult(zdCaseService.run(zdCaseIds));
    }

    @ResponseBody
    @RequestMapping(value = "/case/runTemp.json")
    public DataResult data_case_runTemp(STCase zdCase) {
        return DataResult.successResult(zdCaseService.runCaseNotStored(zdCase));
    }

    @ResponseBody
    @RequestMapping(value = "/case/single.json")
    public DataResult data_case_single(Integer id) {
        return DataResult.successResult(
                zdCaseService.find(
                        new Query().with("id", id)).singleResult());

    }

    @ResponseBody
    @RequestMapping(value = "/case/list.json")
    public DataResult data_case_list(@RequestParam(required = false) Integer projectId,
                                     @RequestParam(required = false) Integer pageIndex,
                                     @RequestParam(required = false) Integer pageSize) {

        Query query =
                new Query()
                        .with("projectId", projectId)
                        .withOrder("id", "desc");
        if (pageIndex != null && pageSize != null) {
            query.withPager(pageIndex, pageSize);
        }

        return DataResult.successResult(
                zdCaseService.find(query));
    }

    @ResponseBody
    @RequestMapping(value = "/case/add.json")
    public DataResult data_case_add(STCase zdCase) {
        if(StringUtils.isEmpty(zdCase.getName())) {
            throw new ServiceResultFail("名称不能为空！");
        }
        return DataResult.successResult(zdCaseService.add(zdCase));
    }

    @ResponseBody
    @RequestMapping(value = "/case/edit.json")
    public DataResult data_case_edit(STCase zdCase) {
        return DataResult.successResult(zdCaseService.edit(zdCase));
    }


    @Transactional
    @ResponseBody
    @RequestMapping(value = "/case/delete.json")
    public DataResult data_case_delete(@RequestParam(value = "zdCaseIds[]") Long[] zdCaseIds) {
        return DataResult.successResult(zdCaseService.delete(zdCaseIds));
    }

}
