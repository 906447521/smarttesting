package smarttesting.action.data;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smarttesting.data.model.STInterface;
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
public class STDataInterfaceController {

    @Resource
    private STUserService      zdUserService;
    @Resource
    private STProjectService   zdProjectService;
    @Resource
    private STInterfaceService zdInterfaceService;
    @Resource
    private STCaseService      zdCaseService;

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
    public DataResult data_interface_add(STInterface zdInterface) {
        if(StringUtils.isEmpty(zdInterface.getName())) {
            throw new ServiceResultFail("名称不能为空！");
        }
        return DataResult.successResult(zdInterfaceService.add(zdInterface));
    }

    @ResponseBody
    @RequestMapping(value = "/interface/edit.json")
    public DataResult data_interface_edit(STInterface zdInterface) {
        return DataResult.successResult(zdInterfaceService.edit(zdInterface));
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "/interface/delete.json")
    public DataResult data_interface_delete(@RequestParam(value = "zdInterfaceIds[]") Long[] zdInterfaceIds) {
        return DataResult.successResult(zdInterfaceService.delete(zdInterfaceIds));
    }

}
