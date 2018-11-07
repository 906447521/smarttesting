package smarttesting.service;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smarttesting.data.ZDTaskMapper;
import smarttesting.data.ZDTaskResultMapper;
import smarttesting.data.model.ZDTask;
import smarttesting.data.model.ZDTaskResult;
import smarttesting.service.job.CaseJob;
import smarttesting.service.job.ClusteredJob;
import smarttesting.service.model.ServiceResultFail;
import smarttesting.utils.JSON;
import smarttesting.utils.Pagination;
import smarttesting.utils.Query;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * @author
 */
@Service
public class ZDTaskService {

    private static Logger log = LoggerFactory.getLogger(ZDTaskService.class);

    @Resource
    private ZDTaskMapper       zdTaskMapper;
    @Resource
    private ZDTaskResultMapper zdTaskResultMapper;

    @Resource
    private ClusteredJob       clusteredJob;
    private ZDTaskResultMapper zdTaskResultMapper1;

    public List<ZDTask> findAll(Query query) {
        List<ZDTask> zdUserList = zdTaskMapper.select(query);
        return zdUserList;
    }

    public Pagination<ZDTask> find(Query query) {

        long count = zdTaskMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteria();
        Pagination<ZDTask> pageResult = new Pagination<ZDTask>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<ZDTask> data = zdTaskMapper.select(query);
            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    @Transactional
    public ZDTask add(ZDTask zdTask) {

        try {
            new CronExpression(zdTask.getCon());
        } catch (ParseException e) {
            throw new ServiceResultFail("表达式不正确！");
        }
        zdTaskMapper.insert(zdTask);
        Long id = zdTask.getId();
        try {
            clusteredJob.remove("c." + id).add("c." + id, CaseJob.class, zdTask.getCon());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceResultFail("添加任务失败！");
        }

        log.info("ADD TASK：{}", JSON.write(zdTask));
        return zdTask;
    }

    @Transactional
    public ZDTask edit(ZDTask zdTask) {

        try {
            new CronExpression(zdTask.getCon());
        } catch (ParseException e) {
            throw new ServiceResultFail("表达式不正确！");
        }
        zdTaskMapper.update(zdTask);
        Long id = zdTask.getId();
        try {
            clusteredJob.remove("c." + id).add("c." + id, CaseJob.class, zdTask.getCon());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceResultFail("添加任务失败！");
        }

        log.info("EDIT TASK：{}", JSON.write(zdTask));
        return zdTask;
    }

    @Transactional
    public ZDTaskResult addZDTaskResult(ZDTaskResult zdTaskResult) {
        zdTaskResultMapper.insert(zdTaskResult);
        return zdTaskResult;
    }

    public Long[] delete(Long[] zdInterfaceIds) {
        for (Long id : zdInterfaceIds) {
            zdTaskMapper.delete(new Query().with("id", id));
        }

        return zdInterfaceIds;
    }


    public ZDTaskResult findZDTaskSingleResult(String rid) {
        List<ZDTaskResult> zdTaskResults = zdTaskResultMapper.select(new Query().with("rid", rid));
        if (zdTaskResults != null && zdTaskResults.size() > 0) {
            return zdTaskResults.get(0);
        }
        return null;
    }


}
