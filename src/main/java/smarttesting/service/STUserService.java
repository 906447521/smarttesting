package smarttesting.service;

import org.springframework.stereotype.Service;
import smarttesting.data.STUserMapper;
import smarttesting.data.model.STUser;
import smarttesting.utils.Pagination;
import smarttesting.utils.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author
 */
@Service
public class STUserService {

    @Resource
    private STUserMapper zdUserMapper;

    public List<STUser> findAll(Query query) {
        List<STUser> zdUserList = zdUserMapper.select(query);
        return zdUserList;
    }

    public Pagination<STUser> find(Query query) {

        long count = zdUserMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteriaIfNullWithMaxInteger();
        Pagination<STUser> pageResult = new Pagination<STUser>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<STUser> data = zdUserMapper.select(query);
            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    public STUser add(STUser zdUser) {
        zdUserMapper.insert(zdUser);
        return zdUser;
    }

    public STUser edit(STUser zdUser) {
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
