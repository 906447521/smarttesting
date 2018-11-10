package smarttesting.action.data;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smarttesting.data.model.STScene;
import smarttesting.service.STInterfaceService;
import smarttesting.service.STProjectService;
import smarttesting.service.STSceneService;
import smarttesting.service.STUserService;
import smarttesting.service.model.DataResult;
import smarttesting.utils.Query;

import javax.annotation.Resource;

/**
 * @author
 */
@RequestMapping(value = "data")
@Controller
public class STDataSceneController {

    @Resource
    private STUserService      zdUserService;
    @Resource
    private STProjectService   zdProjectService;
    @Resource
    private STInterfaceService zdInterfaceService;
    @Resource
    private STSceneService     zdSceneService;


    @ResponseBody
    @RequestMapping(value = "/scene/single.json")
    public DataResult data_scene_single(Integer id) {
        return DataResult.successResult(
                zdSceneService.find(
                        new Query().with("id", id)).singleResult());

    }

    @ResponseBody
    @RequestMapping(value = "/scene/list.json")
    public DataResult data_scene_list(@RequestParam(required = false) Integer projectId,
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
                zdSceneService.find(query));
    }

    @ResponseBody
    @RequestMapping(value = "/scene/add.json")
    public DataResult data_scene_add(STScene zdScene) {
        return DataResult.successResult(zdSceneService.add(zdScene));
    }

    @ResponseBody
    @RequestMapping(value = "/scene/edit.json")
    public DataResult data_scene_edit(STScene zdScene) {
        return DataResult.successResult(zdSceneService.edit(zdScene));
    }


    @Transactional
    @ResponseBody
    @RequestMapping(value = "/scene/delete.json")
    public DataResult data_scene_delete(@RequestParam(value = "zdSceneIds[]") Long[] zdSceneIds) {
        return DataResult.successResult(zdSceneService.delete(zdSceneIds));
    }

}
