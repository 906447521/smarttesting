package smarttesting.service.engine;

import smarttesting.service.model.ServiceResultFail;

/**
 * @author
 */
public class CallerBuilder {

    String type;

    public CallerBuilder(String type) {
        this.type = type;
    }

    // 包括协议， 目前只支持了http

    public Caller build() {

        if ("http".equals(type)) {
            return new CallerHTTP();
        } else if ("dubbo".equals(type)) {
            return new CallerDubbo();
        } else if ("python".equals(type)) {
            return new CallerPython("");
        } else if ("shell".equals(type)) {
            return new CallerShell();
        } else if ("jmeter".equals(type)) {
            return new CallerJMeter("");
        } else {
            throw new ServiceResultFail("不支持的执行模式！");
        }
    }

}
