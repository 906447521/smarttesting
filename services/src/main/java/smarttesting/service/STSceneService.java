package smarttesting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smarttesting.data.STSceneMapper;
import smarttesting.data.model.STScene;
import smarttesting.utils.Pagination;
import smarttesting.utils.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author
 */
@Service
public class STSceneService {

    private static Logger log = LoggerFactory.getLogger(STSceneService.class);

    @Resource
    private STSceneMapper zdSceneMapper;
    @Resource
    private STCaseService zdCaseService;


    public List<STScene> findAll(Query query) {
        List<STScene> zdSceneList = zdSceneMapper.select(query);
        return zdSceneList;
    }

    public Pagination<STScene> find(Query query) {

        long count = zdSceneMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteriaIfNullWithMaxInteger();
        Pagination<STScene> pageResult = new Pagination<STScene>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<STScene> data = zdSceneMapper.select(query);
            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    @Transactional
    public STScene add(STScene zdScene) {
        zdSceneMapper.insert(zdScene);
        return zdScene;
    }

    public STScene edit(STScene zdScene) {
        zdSceneMapper.update(zdScene);
        return zdScene;
    }


    public Long[] delete(Long[] zdSceneIds) {
        for (Long id : zdSceneIds) {
            zdSceneMapper.delete(new Query().with("id", id));
        }

        return zdSceneIds;
    }

}
