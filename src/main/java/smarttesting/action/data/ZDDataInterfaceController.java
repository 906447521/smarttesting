package smarttesting.action.data;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smarttesting.data.model.ZDInterface;
import smarttesting.service.ZDCaseService;
import smarttesting.service.ZDInterfaceService;
import smarttesting.service.ZDProjectService;
import smarttesting.service.ZDUserService;
import smarttesting.service.model.DataResult;
import smarttesting.utils.Query;

import javax.annotation.Resource;

/**
 * @author
 */
@RequestMapping(value = "data")
@Controller
public class ZDDataInterfaceController {

    @Resource
    private ZDUserService      zdUserService;
    @Resource
    private ZDProjectService   zdProjectService;
    @Resource
    private ZDInterfaceService zdInterfaceService;
    @Resource
    private ZDCaseService      zdCaseService;

    @ResponseBody
    @RequestMapping(value = "/interface/single.json")
    public DataResult data_interface_single(Integer id) {
        return DataResult.successResult(
                zdInterfaceService.find(
                        new Query().with("id", id)).singleResult());

    }

    @ResponseBody
    @RequestMapping(value = "/interface/list.json")
    public DataResult data_interface_list(@RequestParam(required = false) Integer projectId,
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
                zdInterfaceService.find(query));
    }

    @ResponseBody
    @RequestMapping(value = "/interface/add.json")
    public DataResult data_interface_add(ZDInterface zdInterface) {
        return DataResult.successResult(zdInterfaceService.add(zdInterface));
    }

    @ResponseBody
    @RequestMapping(value = "/interface/edit.json")
    public DataResult data_interface_edit(ZDInterface zdInterface) {
        return DataResult.successResult(zdInterfaceService.edit(zdInterface));
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "/interface/delete.json")
    public DataResult data_interface_delete(@RequestParam(value = "zdInterfaceIds[]") Long[] zdInterfaceIds) {
        return DataResult.successResult(zdInterfaceService.delete(zdInterfaceIds));
    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/interface/run.json")
    public DataResult data_interface_run(@RequestParam(value = "zdInterfaceIds[]") Long[] zdInterfaceIds) {
        return DataResult.successResult(zdInterfaceService.run(zdInterfaceIds));
    }
}
