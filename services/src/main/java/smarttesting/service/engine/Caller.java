package smarttesting.service.engine;

import smarttesting.service.model.CallerRequest;
import smarttesting.service.model.CallerResult;

/**
 * @author
 */
public interface Caller {
    CallerResult run(CallerRequest request);
}
