package smarttesting.service.engine;

import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.ognl.OgnlException;
import smarttesting.utils.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
public class ToolScriptCollection {

    public Map xml(String str) {
        return new HashMap();
    }

    public Map map(String str) {
        return JSON.read(str, Map.class);
    }

    public List list(String str) {
        return JSON.read(str, List.class);
    }


}
