package smarttesting.service;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import smarttesting.data.ZDCaseMapper;
import smarttesting.data.model.ZDCase;
import smarttesting.data.model.ZDInterface;
import smarttesting.service.engine.CallerHTTP;
import smarttesting.service.model.CallerRequest;
import smarttesting.service.model.CallerResult;
import smarttesting.service.model.ServiceResultFail;
import smarttesting.utils.Pagination;
import smarttesting.utils.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author
 */
@Service
public class ZDCaseService {

    @Resource
    private ZDCaseMapper       zdCaseMapper;
    @Resource
    private ZDInterfaceService zdInterfaceService;

    public List<ZDCase> findAll(Query query) {
        List<ZDCase> zdCaseList = zdCaseMapper.select(query);
        return zdCaseList;
    }

    public Pagination<ZDCase> find(Query query) {

        long count = zdCaseMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteria();
        Pagination<ZDCase> pageResult = new Pagination<ZDCase>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<ZDCase> data = zdCaseMapper.select(query);
            for (ZDCase zdCase : data) {
                Query query1 = new Query();
                query1.put("id", zdCase.getInterfaceId());
                zdCase.setZdInterface(zdInterfaceService.find(query1).singleResult());
            }
            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    public ZDCase add(ZDCase zdCase) {
        zdCaseMapper.insert(zdCase);
        return zdCase;
    }

    public ZDCase edit(ZDCase zdCase) {
        zdCaseMapper.update(zdCase);
        return zdCase;
    }


    public Long[] delete(Long[] zdCaseIds) {
        for (Long id : zdCaseIds) {
            zdCaseMapper.delete(new Query().with("id", id));
        }
        return zdCaseIds;
    }

    public List<CallerResult> run(Long[] zdCaseIds) {

        List<CallerResult> callerResults = Lists.newArrayList();

        for (Long caseId : zdCaseIds) {
            ZDCase zdCase = this.find(new Query().with("id", caseId)).singleResult();
            if (zdCase != null) {
                ZDInterface zdInterface = zdInterfaceService.find(new Query().with("id", zdCase.getInterfaceId())).singleResult();
                if (zdInterface != null) {
                    if ("http".equals(zdInterface.getType())) {
                        CallerRequest request = new CallerRequest();
                        request.setRequestCaseName(zdCase.getName());
                        request.setResponseCharset("utf8");
                        request.setRequestMethod(zdInterface.getMethod());
                        request.setRequestURL(zdInterface.getUrl() + (zdCase.getUrlSuffix() == null ? "" : zdCase.getUrlSuffix()));
                        request.setRequestHeaderProperties(zdInterface.getRequestHeader());
                        request.setRequestBody(zdCase.getRequestBody());
                        request.setRequestContentType(zdCase.getContentType());
                        request.setResponseCharset(zdInterface.getResponseCharset());
                        request.setResultScript(zdCase.getResultScript());

                        CallerResult result = new CallerHTTP().run(request);

                        callerResults.add(result);
                    }
                }

            }

        }


        return callerResults;
    }

    public List<CallerResult> run(ZDCase zdCase) {

        List<CallerResult> callerResults = Lists.newArrayList();
        if (zdCase == null || zdCase.getInterfaceId() == null) {
            throw new ServiceResultFail("参数不全");
        }

        if (zdCase != null) {
            ZDInterface zdInterface = zdInterfaceService.find(new Query().with("id", zdCase.getInterfaceId())).singleResult();
            if (zdInterface != null) {
                if ("http".equals(zdInterface.getType())) {
                    CallerRequest request = new CallerRequest();
                    request.setRequestCaseName(zdCase.getName());
                    request.setResponseCharset("utf8");
                    request.setRequestMethod(zdInterface.getMethod());
                    request.setRequestURL(zdInterface.getUrl() + (zdCase.getUrlSuffix() == null ? "" : zdCase.getUrlSuffix()));
                    request.setRequestHeaderProperties(zdInterface.getRequestHeader());
                    request.setRequestBody(zdCase.getRequestBody());
                    request.setRequestContentType(zdCase.getContentType());
                    request.setResponseCharset(zdInterface.getResponseCharset());
                    request.setResultScript(zdCase.getResultScript());

                    CallerResult result = new CallerHTTP().run(request);

                    callerResults.add(result);
                }
            }
        }
        return callerResults;
    }
}
