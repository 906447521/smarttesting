package smarttesting.service.engine;

/**
 * @author
 */
public class ToolScriptString {

    String ResponseBody;

    ToolScriptString(String ResponseBody) {
        this.ResponseBody = ResponseBody;
    }

    public Boolean notContains(String src, String str) {

        return !contains(src, str);

    }

    public Boolean notContains(String str) {
        return !contains(str);
    }

    public Boolean contains(String src, String str) {

        if (src == null) {
            return false;
        }

        return src.indexOf(str) > -1;

    }

    public Boolean contains(String str) {

        if (ResponseBody == null) {
            return false;
        }

        return ResponseBody.indexOf(str) > -1;

    }

}
