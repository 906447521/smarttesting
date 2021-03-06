package smarttesting.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smarttesting.service.STTestService;
import smarttesting.service.model.DataResult;
import smarttesting.service.model.STTest;
import smarttesting.utils.HTTP;
import smarttesting.utils.JSON;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Controller
@RequestMapping(value = "data")
public class STTestController {

    @Resource
    STTestService stTestService;

    @ResponseBody
    @RequestMapping(value = "/test/url.json")
    public DataResult test_url(@RequestParam String url) {
        try {
            HTTP.Response
                    response = HTTP.open(url).send(null);
            return DataResult.successResult(response.getStatus());
        } catch (Exception e) {
            return DataResult.errorResult("0");
        }
    }

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
    public DataResult test_domain(STTest zdTest) {
        return DataResult.successResult(zdTest);
    }

    @ResponseBody
    @RequestMapping(value = "/test/domains.json")
    public DataResult test_domains(@RequestBody List<STTest> zdTest) {
        return DataResult.successResult(zdTest);
    }

    @ResponseBody
    @RequestMapping(value = "/test/vector.json")
    public DataResult test_vector(@RequestParam(value = "ids[]") Long[] ids) {
        return DataResult.successResult(JSON.write(ids));
    }


    @ResponseBody
    @RequestMapping(value = "/test/transaction.json")
    public DataResult test_transaction() {
        stTestService.test();
        return null;
    }

}
