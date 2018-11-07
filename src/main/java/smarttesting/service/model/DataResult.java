package smarttesting.service.model;

/**
 * @author
 */
public class DataResult {

    private int    code;
    private String message;
    private Object data;

    private static final int CODE_SUCCESS      = 0;
    private static final int CODE_ERROR_COMMON = 1;

    public static final DataResult successResult(Object result) {
        return new DataResult(CODE_SUCCESS, result);
    }

    public static final DataResult successResult(Object result, String message) {
        return new DataResult(CODE_SUCCESS, message, result);
    }

    public static final DataResult errorResult(String message) {
        return new DataResult(CODE_ERROR_COMMON, message);
    }

    public static final DataResult errorResult(int errorCode, String message) {
        return new DataResult(errorCode, message);
    }


    public DataResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public DataResult(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public DataResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
