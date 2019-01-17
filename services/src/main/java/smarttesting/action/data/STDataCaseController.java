package smarttesting.action.data;

import com.google.common.collect.Lists;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smarttesting.data.model.STCase;
import smarttesting.data.model.STInterface;
import smarttesting.data.model.STProject;
import smarttesting.service.STCaseService;
import smarttesting.service.STInterfaceService;
import smarttesting.service.STProjectService;
import smarttesting.service.STUserService;
import smarttesting.service.model.DataResult;
import smarttesting.service.model.ServiceResultFail;
import smarttesting.utils.Query;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
        if (StringUtils.isEmpty(zdCase.getName())) {
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

    @ResponseBody
    @RequestMapping(value = "/case/export_all.json")
    public void data_case_export_all(HttpServletResponse response, Integer projectId) {

        try {

            List<STCase> cases = zdCaseService.findAll(
                    new Query()
                            .with("projectId", projectId));

            HashMap interfaceCacheHashMap = new HashMap();

            STProject stProject = zdProjectService.find(
                    new Query()
                            .with("id", projectId)).singleResult();


            List<Object[]> listArray = Lists.newArrayList();
            int i = 1;
            for (STCase stCase : cases) {

                STInterface stInterface = null;
                Long stCaseInterfaceId = stCase.getInterfaceId();
                if (stCaseInterfaceId != null) {
                    stInterface = (STInterface) interfaceCacheHashMap.get(stCaseInterfaceId);
                    if (stInterface == null) {
                        stInterface = zdInterfaceService.find(
                                new Query()
                                        .with("id", stCaseInterfaceId)).singleResult();
                    }
                }

                if (stInterface == null) {
                    stInterface = new STInterface();
                }

                Object[] fields = {
                        i,
                        stCase.getName(),
                        (stInterface.getUrl() == null ? "" : stInterface.getUrl().trim()) + (stCase.getUrlSuffix() == null ? "" : stCase.getUrlSuffix().trim()),
                        stInterface.getMethod(),
                        "UTF-8",
                        stInterface.getResponseCharset(),
                        stCase.getContentType(),
                        stInterface.getRequestHeader(),
                        stCase.getRequestBody(),
                        stCase.getResultScript(),
                };
                listArray.add(fields);
                i++;

            }

            String fileName = stProject.getName() + "-测试用例-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            fileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName + ".csv");
            response.setContentType("application/csv;charset=GBK");

            final String[] headers = {
                    "ID", "Name", "URL", "Method", "Request Charset", "Response Charset", "ContentType", "RequestHeader", "RequestBody", "ResultScript"};
            CSVPrinter printer =
                    CSVFormat.DEFAULT
                            .withHeader(headers).print(
                            new OutputStreamWriter(response.getOutputStream(), "GBK"));
            printer.printRecords(listArray);
            printer.flush();

        } catch (Exception e) {

        }

    }
}
