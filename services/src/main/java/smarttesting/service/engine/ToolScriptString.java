package smarttesting.service.engine;

/**
 * @author
 */
public class ToolScriptString {

   public Boolean contains(String src, String str) {

        if (src == null) {
            return false;
        }

        return src.indexOf(str) > -1;

    }

}
