package smarttesting.service.model;

/**
 * @author
 */
public class ServiceResultFail extends RuntimeException {

    public ServiceResultFail(String message) {
        super(message);
    }

    public ServiceResultFail(Throwable message) {
        super(message);
    }
}
