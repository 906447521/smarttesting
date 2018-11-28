package smarttesting.service.engine;

import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ScriptOgnlTest {

    @Test
    public void doScript() {
        OgnlContext context = new OgnlContext();
        Map map = new HashMap();
        context.setValues(map);
        String s = "\"\"==\"<script\"";
        try {
            System.out.println(
                    Ognl.getValue(
                            s,
                            context,
                            context.getRoot()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}