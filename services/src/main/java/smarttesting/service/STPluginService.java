package smarttesting.service;

import org.springframework.stereotype.Service;
import smarttesting.data.STPluginMapper;
import smarttesting.data.model.STPlugin;
import smarttesting.utils.Pagination;
import smarttesting.utils.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author
 */
@Service
public class STPluginService {

    @Resource
    private STPluginMapper zdPluginMapper;

    public Pagination<STPlugin> find(Query query) {

        long count = zdPluginMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteriaIfNullWithMaxInteger();
        Pagination<STPlugin> pageResult = new Pagination<STPlugin>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<STPlugin> data = zdPluginMapper.select(query);
            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    public STPlugin edit(STPlugin zdPlugin) {
        zdPluginMapper.update(zdPlugin);
        return zdPlugin;
    }

}
