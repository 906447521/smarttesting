package smarttesting.service;

import org.springframework.stereotype.Service;
import smarttesting.data.ZDUserMapper;
import smarttesting.data.model.ZDUser;
import smarttesting.utils.Pagination;
import smarttesting.utils.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author
 */
@Service
public class ZDUserService {

    @Resource
    private ZDUserMapper zdUserMapper;

    public List<ZDUser> findAll(Query query) {
        List<ZDUser> zdUserList = zdUserMapper.select(query);
        return zdUserList;
    }

    public Pagination<ZDUser> find(Query query) {

        long count = zdUserMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteria();
        Pagination<ZDUser> pageResult = new Pagination<ZDUser>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<ZDUser> data = zdUserMapper.select(query);
            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    public ZDUser add(ZDUser zdUser) {
        zdUserMapper.insert(zdUser);
        return zdUser;
    }

    public ZDUser edit(ZDUser zdUser) {
        zdUserMapper.update(zdUser);
        return zdUser;
    }

    public Long[] delete(Long[] zdUserIds) {

        for (Long id : zdUserIds) {
            zdUserMapper.delete(new Query().with("id", id));
        }

        return zdUserIds;
    }
}
