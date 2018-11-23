package smarttesting.action.data;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smarttesting.data.model.STPlugin;
import smarttesting.service.*;
import smarttesting.service.model.DataResult;
import smarttesting.service.model.ServiceResultFail;
import smarttesting.utils.Query;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author
 */
@RequestMapping(value = "data")
@Controller
public class STDataPluginController {

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
    @RequestMapping(value = "/plugin/single.json")
    public DataResult data_plugin_single(Integer id) {
        return DataResult.successResult(
                zdPluginService.find(
                        new Query().with("id", id)).singleResult());

    }

    @ResponseBody
    @RequestMapping(value = "/plugin/list.json")
    public DataResult data_plugin_list(@RequestParam(required = false) Integer interfaceId, Integer pageIndex, Integer pageSize) {
        return DataResult.successResult(
                zdPluginService.find(
                        new Query()
                                .withPager(pageIndex, pageSize)
                                .withOrder("id", "desc")));
    }

    @ResponseBody
    @RequestMapping(value = "/plugin/edit.json")
    public DataResult data_plugin_edit(STPlugin zdPlugin) {

        File file = new File(zdPlugin.getHome());
        if (!file.isDirectory()) {
            throw new ServiceResultFail("目录不是文件夹，请选择如：/developer/servers/apache-jmeter-5.0");
        }

        return DataResult.successResult(zdPluginService.edit(zdPlugin));
    }


}
