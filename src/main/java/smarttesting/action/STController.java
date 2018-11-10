package smarttesting.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author
 */
@Controller
public class STController {

    @RequestMapping(value = {"", "home.html"})
    public ModelAndView home() {
        ModelAndView model = new ModelAndView("/index");
        return model;
    }

    @RequestMapping(value = "user/list.html")
    public ModelAndView user_list() {
        ModelAndView model = new ModelAndView("/user/list");
        return model;
    }

    @RequestMapping(value = "plugin/list.html")
    public ModelAndView plugin_list() {
        ModelAndView model = new ModelAndView("/plugin/list");
        return model;
    }

    @RequestMapping(value = "project/list.html")
    public ModelAndView project_list() {
        ModelAndView model = new ModelAndView("/project/list");
        return model;
    }

    @RequestMapping(value = "project/detail_task.html")
    public ModelAndView project_detail_task(Integer id) {
        ModelAndView model = new ModelAndView("/project/detail_task");
        model.addObject("id", id);
        return model;
    }

    @RequestMapping(value = "project/detail_interface.html")
    public ModelAndView project_detail_interface(Integer id) {
        ModelAndView model = new ModelAndView("/project/detail_interface");
        model.addObject("id", id);
        return model;
    }

    @RequestMapping(value = "project/detail_case.html")
    public ModelAndView project_detail_case(Integer id) {
        ModelAndView model = new ModelAndView("/project/detail_case");
        model.addObject("id", id);
        return model;
    }

    @RequestMapping(value = "project/detail_member.html")
    public ModelAndView project_detail_member(Integer id) {
        ModelAndView model = new ModelAndView("/project/detail_member");
        model.addObject("id", id);
        return model;
    }

    @RequestMapping(value = "project/detail_scene.html")
    public ModelAndView project_detail_scene(Integer id) {
        ModelAndView model = new ModelAndView("/project/detail_scene");
        model.addObject("id", id);
        return model;
    }

    @RequestMapping(value = "interface/detail.html")
    public ModelAndView interface_detail(Integer id) {
        ModelAndView model = new ModelAndView("/interface/detail");
        return model;
    }

    @RequestMapping(value = "case/detail.html")
    public ModelAndView case_detail(Integer id) {
        ModelAndView model = new ModelAndView("/case/detail");
        return model;
    }

    @RequestMapping(value = "task/detail.html")
    public ModelAndView task_detail(Integer id) {
        ModelAndView model = new ModelAndView("/task/detail");
        return model;
    }

    @RequestMapping(value = "scene/detail.html")
    public ModelAndView scene_detail(Integer id) {
        ModelAndView model = new ModelAndView("/scene/detail");
        return model;
    }

    @RequestMapping(value = "task_result/detail.html")
    public ModelAndView task_result_detail(Integer id) {
        ModelAndView model = new ModelAndView("/task_result/detail");
        return model;
    }
}
