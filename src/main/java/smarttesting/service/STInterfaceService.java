package smarttesting.service;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import smarttesting.data.STInterfaceMapper;
import smarttesting.data.model.STCase;
import smarttesting.data.model.STInterface;
import smarttesting.service.engine.CallerHTTP;
import smarttesting.service.model.CallerRequest;
import smarttesting.service.model.CallerResult;
import smarttesting.utils.Pagination;
import smarttesting.utils.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author
 */
@Service
public class STInterfaceService {

    @Resource
    private STInterfaceMapper zdInterfaceMapper;
    @Resource
    private STCaseService     zdCaseService;

    public List<STInterface> findAll(Query query) {
        List<STInterface> zdInterfaceList = zdInterfaceMapper.select(query);
        return zdInterfaceList;
    }

    public Pagination<STInterface> find(Query query) {

        long count = zdInterfaceMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteriaIfNullWithMaxInteger();
        Pagination<STInterface> pageResult = new Pagination<STInterface>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<STInterface> data = zdInterfaceMapper.select(query);
            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    public STInterface add(STInterface zdInterface) {
        zdInterfaceMapper.insert(zdInterface);
        return zdInterface;
    }

    public STInterface edit(STInterface zdInterface) {
        zdInterfaceMapper.update(zdInterface);

        List<STCase> zdCaseList = zdCaseService.findAll(new Query().with("interfaceId", zdInterface.getId()));
        for (STCase zdCase : zdCaseList) {
            zdCase.setMethod(zdInterface.getMethod());
            zdCase.setUrl(zdInterface.getUrl());
            zdCaseService.edit(zdCase);
        }

        return zdInterface;
    }

    public Long[] delete(Long[] zdInterfaceIds) {

        for (Long id : zdInterfaceIds) {
            zdInterfaceMapper.delete(new Query().with("id", id));
        }

        return zdInterfaceIds;
    }

    @Deprecated
    public List<CallerResult> run(Long[] zdInterfaceIds) {

        List<CallerResult> callerResults = Lists.newArrayList();

        for (Long interfaceId : zdInterfaceIds) {

            STInterface zdInterface = this.find(new Query().with("id", interfaceId)).singleResult();
            if (zdInterface != null) {
                List<STCase> zdCases = zdCaseService.find(new Query().with("interfaceId", interfaceId)).getData();
                for (STCase zdCase : zdCases) {
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

}
