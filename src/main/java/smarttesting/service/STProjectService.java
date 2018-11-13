package smarttesting.service;

import org.springframework.stereotype.Service;
import smarttesting.data.*;
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
public class STProjectService {

    @Resource
    private STProjectMapper     zdProjectMapper;
    @Resource
    private STCaseMapper        zdCaseMapper;
    @Resource
    private STInterfaceService  zdInterfaceService;
    @Resource
    private STInterfaceMapper   zdInterfaceMapper;
    @Resource
    private STCaseService       zdCaseService;
    @Resource
    private STProjectUserMapper zdProjectUserMapper;
    @Resource
    private STUserService       zdUserService;
    @Resource
    private STSceneService      zdSceneService;
    @Resource
    private STSceneMapper       zdSceneMapper;

    public List<STProject> findAll(Query query) {
        List<STProject> zdProjectList = zdProjectMapper.select(query);
        return zdProjectList;
    }

    public Pagination<STProject> findAllPromotion(Query query) {
        Pagination<STProject> pageResult = new Pagination<STProject>(1, 9999, 9999);
        List<STProject> zdProjects = zdProjectMapper.select(query);
        for (STProject zdProject : zdProjects) {
            Query query1 = new Query();
            query1.put("projectId", zdProject.getId());
            zdProject.setCaseCount(zdCaseMapper.count(query1));
        }
        pageResult.withResult(zdProjects, (int) zdProjects.size());
        return pageResult;
    }

    public Pagination<STProject> find(Query query) {

        long count = zdProjectMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteriaIfNullWithMaxInteger();
        Pagination<STProject> pageResult = new Pagination<STProject>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<STProject> data = zdProjectMapper.select(query);

            for (STProject zdProject : data) {
                Query query1 = new Query();
                query1.put("projectId", zdProject.getId());
                zdProject.setCaseCount(zdCaseMapper.count(query1));
            }

            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    public STProject add(STProject zdProject) {
        zdProjectMapper.insert(zdProject);
        STProjectUser zdProjectUser = new STProjectUser();
        zdProjectUser.setUserName(zdProject.getCreator());
        zdProjectUser.setProjectId(zdProject.getId());
        //设置角色为创建者
        zdProjectUser.setRole(0);
        zdProjectUserMapper.insert(zdProjectUser);
        return zdProject;
    }

    public STProject edit(STProject zdProject) {
        zdProjectMapper.update(zdProject);
        return zdProject;
    }

    public Long[] delete(Long[] zdProjectIds) {

        for (Long id : zdProjectIds) {

            // 删除项目
            zdProjectMapper.delete(new Query().with("id", id));

            // 删除接口
            List<STInterface> zdInterfaces = zdInterfaceMapper.select(new Query().with("projectId", id));
            if (zdInterfaces.size() > 0) {
                for (STInterface zdInterface : zdInterfaces) {
                    zdInterfaceMapper.delete(new Query().with("id", zdInterface.getId()));
                }
            }

            // 删除用例
            List<STCase> zdCases = zdCaseService.find(new Query().with("projectId", id)).getData();
            if (zdCases.size() > 0) {
                for (STCase zdCase : zdCases) {
                    zdCaseMapper.delete(new Query().with("id", zdCase.getId()));
                }
            }

            // 删除场景
            List<STScene> stScenes = zdSceneService.find(new Query().with("projectId", id)).getData();
            if (stScenes.size() > 0) {
                for (STScene stScene : stScenes) {
                    zdSceneMapper.delete(new Query().with("id", stScene.getId()));
                }
            }


            // 删除用户
            List<STProjectUser> zdProjectUsers = zdProjectUserMapper.select(new Query().with("projectId", id));
            if (zdProjectUsers.size() > 0) {
                for (STProjectUser ZDProjectUser : zdProjectUsers) {
                    zdProjectUserMapper.delete(new Query().with("id", ZDProjectUser.getId()));
                }
            }

        }

        return zdProjectIds;
    }


    public List<STProjectUser> findMember(Query query) {
        if (query.get("projectId") == null) {
            throw new ServiceResultFail("未选择项目！");
        }
        List<STProjectUser> zdProjectUsers = zdProjectUserMapper.select(query);
        return zdProjectUsers;
    }


    public STProjectUser addMember(STProjectUser zdProjectUser) {
        if (zdProjectUser.getUserName() == null) {
            throw new ServiceResultFail("用户不存在！");
        }
        STUser zdUser = zdUserService.find(new Query().with("name", zdProjectUser.getUserName())).singleResult();
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
