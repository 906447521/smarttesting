package smarttesting.service;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import smarttesting.data.STCaseMapper;
import smarttesting.data.model.STCase;
import smarttesting.data.model.STInterface;
import smarttesting.service.engine.CallerBuilder;
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
public class STCaseService {

    @Resource
    private STCaseMapper       zdCaseMapper;
    @Resource
    private STInterfaceService zdInterfaceService;

    public List<STCase> findAll(Query query) {
        List<STCase> zdCaseList = zdCaseMapper.select(query);
        return zdCaseList;
    }

    public Pagination<STCase> find(Query query) {

        long count = zdCaseMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteriaIfNullWithMaxInteger();
        Pagination<STCase> pageResult = new Pagination<STCase>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<STCase> data = zdCaseMapper.select(query);
            for (STCase zdCase : data) {
                Query query1 = new Query();
                query1.put("id", zdCase.getInterfaceId());
                zdCase.setZdInterface(zdInterfaceService.find(query1).singleResult());
            }
            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    public STCase add(STCase zdCase) {
        if (zdCase.getInterfaceId() == null) {
            throw new ServiceResultFail("接口不能为空");
        }
        zdCaseMapper.insert(zdCase);
        return zdCase;
    }

    public STCase edit(STCase zdCase) {
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
            STCase zdCase = this.find(new Query().with("id", caseId)).singleResult();
            if (zdCase != null) {
                callerResults.add(runCase(zdCase));
            }

        }

        return callerResults;
    }

    public List<CallerResult> runCaseNotStored(STCase zdCase) {
        List<CallerResult> callerResults = Lists.newArrayList();
        callerResults.add(runCase(zdCase));
        return callerResults;
    }

    // 执行用例的代码在这个地方添加

    public CallerResult runCase(STCase zdCase) {


        if (zdCase == null || zdCase.getInterfaceId() == null) {
            throw new ServiceResultFail("参数不全");
        }

        STInterface zdInterface = zdInterfaceService.find(new Query().with("id", zdCase.getInterfaceId())).singleResult();

        if (zdInterface == null) {
            throw new ServiceResultFail("参数不全[接口为空]");
        }

        CallerRequest request = new CallerRequest();
        request.setRequestCaseName(zdCase.getName());
        request.setRequestCharset("utf8");
        request.setRequestMethod(zdInterface.getMethod());
        request.setRequestURL((zdInterface.getUrl() == null ? "" : zdInterface.getUrl().trim()) + (zdCase.getUrlSuffix() == null ? "" : zdCase.getUrlSuffix().trim()));
        request.setRequestHeaderProperties(zdInterface.getRequestHeader());
        request.setRequestBody(zdCase.getRequestBody());
        request.setRequestContentType(zdCase.getContentType());
        request.setResponseCharset(zdInterface.getResponseCharset());
        request.setResultScript(zdCase.getResultScript());

        return
                new CallerBuilder(zdInterface.getType())
                        .build()
                        .run(request);
    }

}
