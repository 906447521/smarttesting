package smarttesting.service;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import smarttesting.data.ZDInterfaceMapper;
import smarttesting.data.model.ZDCase;
import smarttesting.data.model.ZDInterface;
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
public class ZDInterfaceService {

    @Resource
    private ZDInterfaceMapper zdInterfaceMapper;
    @Resource
    private ZDCaseService     zdCaseService;

    public List<ZDInterface> findAll(Query query) {
        List<ZDInterface> zdInterfaceList = zdInterfaceMapper.select(query);
        return zdInterfaceList;
    }

    public Pagination<ZDInterface> find(Query query) {

        long count = zdInterfaceMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteria();
        Pagination<ZDInterface> pageResult = new Pagination<ZDInterface>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<ZDInterface> data = zdInterfaceMapper.select(query);
            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    public ZDInterface add(ZDInterface zdInterface) {
        zdInterfaceMapper.insert(zdInterface);
        return zdInterface;
    }

    public ZDInterface edit(ZDInterface zdInterface) {
        zdInterfaceMapper.update(zdInterface);

        List<ZDCase> zdCaseList = zdCaseService.findAll(new Query().with("interfaceId", zdInterface.getId()));
        for (ZDCase zdCase : zdCaseList) {
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

            ZDInterface zdInterface = this.find(new Query().with("id", interfaceId)).singleResult();
            if (zdInterface != null) {
                List<ZDCase> zdCases = zdCaseService.find(new Query().with("interfaceId", interfaceId)).getData();
                for (ZDCase zdCase : zdCases) {
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
