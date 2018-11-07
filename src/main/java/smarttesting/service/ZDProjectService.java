package smarttesting.service;

import org.springframework.stereotype.Service;
import smarttesting.data.ZDCaseMapper;
import smarttesting.data.ZDInterfaceMapper;
import smarttesting.data.ZDProjectMapper;
import smarttesting.data.ZDProjectUserMapper;
import smarttesting.data.model.*;
import smarttesting.service.model.ServiceResultFail;
import smarttesting.utils.Pagination;
import smarttesting.utils.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author
 */
@Service
public class ZDProjectService {

    @Resource
    private ZDProjectMapper     zdProjectMapper;
    @Resource
    private ZDCaseMapper        zdCaseMapper;
    @Resource
    private ZDInterfaceService  zdInterfaceService;
    @Resource
    private ZDInterfaceMapper   zdInterfaceMapper;
    @Resource
    private ZDCaseService       zdCaseService;
    @Resource
    private ZDProjectUserMapper zdProjectUserMapper;
    @Resource
    private ZDUserService       zdUserService;


    public List<ZDProject> findAll(Query query) {
        List<ZDProject> zdProjectList = zdProjectMapper.select(query);
        return zdProjectList;
    }

    public Pagination<ZDProject> findAllPromotion(Query query) {
        Pagination<ZDProject> pageResult = new Pagination<ZDProject>(1, 9999, 9999);
        List<ZDProject> zdProjects = zdProjectMapper.select(query);
        for (ZDProject zdProject : zdProjects) {
            Query query1 = new Query();
            query1.put("projectId", zdProject.getId());
            zdProject.setCaseCount(zdCaseMapper.count(query1));
        }
        pageResult.withResult(zdProjects, (int) zdProjects.size());
        return pageResult;
    }

    public Pagination<ZDProject> find(Query query) {

        long count = zdProjectMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteria();
        Pagination<ZDProject> pageResult = new Pagination<ZDProject>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<ZDProject> data = zdProjectMapper.select(query);

            for (ZDProject zdProject : data) {
                Query query1 = new Query();
                query1.put("projectId", zdProject.getId());
                zdProject.setCaseCount(zdCaseMapper.count(query1));
            }

            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    public ZDProject add(ZDProject zdProject) {
        zdProjectMapper.insert(zdProject);
        ZDProjectUser zdProjectUser = new ZDProjectUser();
        zdProjectUser.setUserName(zdProject.getCreator());
        zdProjectUser.setProjectId(zdProject.getId());
        //设置角色为创建者
        zdProjectUser.setRole(0);
        zdProjectUserMapper.insert(zdProjectUser);
        return zdProject;
    }

    public ZDProject edit(ZDProject zdProject) {
        zdProjectMapper.update(zdProject);
        return zdProject;
    }

    public Long[] delete(Long[] zdProjectIds) {

        for (Long id : zdProjectIds) {

            // 删除项目
            zdProjectMapper.delete(new Query().with("id", id));

            // 删除接口
            List<ZDInterface> zdInterfaces = zdInterfaceMapper.select(new Query().with("projectId", id));
            if (zdInterfaces.size() > 0) {
                for (ZDInterface zdInterface : zdInterfaces) {
                    zdInterfaceMapper.delete(new Query().with("id", zdInterface.getId()));
                }
            }

            // 删除用例
            List<ZDCase> zdCases = zdCaseService.find(new Query().with("projectId", id)).getData();
            if (zdCases.size() > 0) {
                for (ZDCase zdCase : zdCases) {
                    zdCaseMapper.delete(new Query().with("id", zdCase.getId()));
                }
            }

            // 删除用户
            List<ZDProjectUser> zdProjectUsers = zdProjectUserMapper.select(new Query().with("projectId", id));
            if (zdProjectUsers.size() > 0) {
                for (ZDProjectUser ZDProjectUser : zdProjectUsers) {
                    zdProjectUserMapper.delete(new Query().with("id", ZDProjectUser.getId()));
                }
            }

        }

        return zdProjectIds;
    }


    public List<ZDProjectUser> findMember(Query query) {
        if (query.get("projectId") == null) {
            throw new ServiceResultFail("未选择项目！");
        }
        List<ZDProjectUser> zdProjectUsers = zdProjectUserMapper.select(query);
        return zdProjectUsers;
    }


    public ZDProjectUser addMember(ZDProjectUser zdProjectUser) {
        if (zdProjectUser.getUserName() == null) {
            throw new ServiceResultFail("用户不存在！");
        }
        ZDUser zdUser = zdUserService.find(new Query().with("name", zdProjectUser.getUserName())).singleResult();
        if (zdUser == null) {
            throw new ServiceResultFail("用户不存在！");
        }
        //设置角色为成员
        zdProjectUser.setRole(1);
        zdProjectUserMapper.insert(zdProjectUser);
        return zdProjectUser;
    }

    public Long[] deleteMember(Long[] zdProjectUserIds) {
        for (Long id : zdProjectUserIds) {
            zdProjectUserMapper.delete(new Query().with("id", id));
        }
        return zdProjectUserIds;
    }

}
