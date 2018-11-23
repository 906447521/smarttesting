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
public class ScriptOgnl {

    public static Object doScript(String respCode, String respBody, String script) {
        if (script == null || "".equals(script.trim())) {
            return "未添加脚本";
        }
        Object respObj = JSON.read(respBody, Map.class);
        respObj = respObj == null ? JSON.read(respBody, List.class) : respObj;
        OgnlContext context = new OgnlContext();
        Map map = new HashMap();
        map.put("Code", respCode);
        map.put("ResponseBody", respBody);
        map.put("ResponseObject", respObj == null ? respBody : respObj);
        map.put("Collection", new ToolScriptCollection());
        map.put("String", new ToolScriptString());
        context.setValues(map);
        try {
            return Ognl.getValue(script, context, context.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
            return "脚本执行异常：" + e.getMessage();
        }
    }

    public static void main(String[] args) throws OgnlException {

        System.out.println(
                doScript(
                        "200",
                        "{\"code\":0,\"message\":null,\"data\":{\"a\":\"2\",\"b\":\"3\",\"c\":{}}}",
                        "#String.contains('abc', 'ac') && #Code==200 && #ResponseObject.data.a==\"2\" && #ResponseBody==\"{\\\"code\\\":0,\\\"message\\\":null,\\\"data\\\":{\\\"a\\\":\\\"2\\\",\\\"b\\\":\\\"3\\\",\\\"c\\\":{}}}\"")
        );

    }
}
