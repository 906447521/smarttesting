package smarttesting.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smarttesting.service.model.DataResult;
import smarttesting.service.model.ZDTest;
import smarttesting.utils.JSON;

import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Controller
@RequestMapping(value = "data")
public class ZDTestController {

    @ResponseBody
    @RequestMapping(value = "/test/map.json")
    public DataResult test_map(@RequestParam Map map) {
        return DataResult.successResult(map);
    }

    @ResponseBody
    @RequestMapping(value = "/test/map_json.json")
    public DataResult test_map_json(@RequestBody Map map) {
        return DataResult.successResult(map);
    }

    @ResponseBody
    @RequestMapping(value = "/test/parameter.json")
    public DataResult test_parameter(@RequestParam Integer id, @RequestParam String name) {
        return DataResult.successResult(new Object[]{id, name});
    }

    @ResponseBody
    @RequestMapping(value = "/test/domain.json")
    public DataResult test_domain(ZDTest zdTest) {
        return DataResult.successResult(zdTest);
    }

    @ResponseBody
    @RequestMapping(value = "/test/domains.json")
    public DataResult test_domains(@RequestBody List<ZDTest> zdTest) {
        return DataResult.successResult(zdTest);
    }

    @ResponseBody
    @RequestMapping(value = "/test/vector.json")
    public DataResult test_vector(@RequestParam(value = "ids[]") Long[] ids) {
        return DataResult.successResult(JSON.write(ids));
    }
}
