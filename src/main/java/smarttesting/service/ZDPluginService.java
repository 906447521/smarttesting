package smarttesting.service;

import org.springframework.stereotype.Service;
import smarttesting.data.ZDPluginMapper;
import smarttesting.data.model.ZDPlugin;
import smarttesting.utils.Pagination;
import smarttesting.utils.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author
 */
@Service
public class ZDPluginService {

    @Resource
    private ZDPluginMapper zdPluginMapper;

    public Pagination<ZDPlugin> find(Query query) {

        long count = zdPluginMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteria();
        Pagination<ZDPlugin> pageResult = new Pagination<ZDPlugin>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<ZDPlugin> data = zdPluginMapper.select(query);
            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    public ZDPlugin edit(ZDPlugin zdPlugin) {
        zdPluginMapper.update(zdPlugin);
        return zdPlugin;
    }

}
