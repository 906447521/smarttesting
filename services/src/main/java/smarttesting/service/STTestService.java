package smarttesting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smarttesting.data.STUserMapper;
import smarttesting.data.model.STUser;

import javax.annotation.Resource;

/**
 * @author
 */
@Service
public class STTestService {

    @Resource
    private STUserMapper zdUserMapper;

    @Transactional
    public void test() {
        STUser stUser = new STUser();
        stUser.setName("test_transaction");
        stUser.setPwd("");
        stUser.setGroup("test");
        zdUserMapper.insert(stUser);
        throw new RuntimeException();
    }

}
